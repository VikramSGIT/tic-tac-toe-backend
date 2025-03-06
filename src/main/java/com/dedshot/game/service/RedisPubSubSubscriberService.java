package com.dedshot.game.service;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubSubscriberService implements MessageListener{

    @lombok.NonNull private GameSocketService socketService;
    private final GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
    @Override
    public void onMessage(@NonNull Message message, @Nullable byte[] pattern) {
        Object obj = serializer.deserialize(message.getBody());
        if(obj == null) return;
        Map<String, Object> commands = ServiceUtils.saftCastCommand(obj);

        for(Entry<String, Object> command : commands.entrySet()) {
            if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_REMOVE)) {
                try {
                    socketService.removeConnection((String) command.getValue());
                } catch (IOException e) {
                    log.error("Error occured while excuting {} command", CommonConstants.PLAYER_COMMAND_REMOVE);
                    e.printStackTrace();
                }
            } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_REPLACE)) {
                try {
                    socketService.replaceConnection((String) command.getValue());
                } catch (IOException e) {
                    log.error("Error occured while excuting {} command", CommonConstants.PLAYER_COMMAND_REPLACE);
                    e.printStackTrace();
                }
            } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_PUT)) {
                try {
                    socketService.broadcast(ServiceUtils.toJSONString(command.getValue()));
                } catch (IOException e) {
                    log.error("Error occured while excuting {} command", CommonConstants.PLAYER_COMMAND_PUT);
                    e.printStackTrace();
                }
            } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_UPDATE)) {
                try {
                    Map<Integer, String> player = (Map<Integer, String>) command.getValue();
                    socketService.broadcast(ServiceUtils.toJSONString(player));
                } catch (IOException e) {
                    log.error("Error occured while excuting {} command", CommonConstants.PLAYER_COMMAND_PUT);
                    e.printStackTrace();
                }
            }
        }
        log.debug(new String(message.getBody()));
    }
}
