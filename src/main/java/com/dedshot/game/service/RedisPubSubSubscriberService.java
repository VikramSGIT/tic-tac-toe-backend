package com.dedshot.game.service;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.dao.GameState;
import com.dedshot.game.errors.PlayerInvalidException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPubSubSubscriberService implements MessageListener{

    @NonNull private final GameState gameState;
    @NonNull private GameSocketService socketService;
    private final JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();

    @Override
    public void onMessage(@org.springframework.lang.NonNull Message message, @Nullable byte[] pattern) {
        Object obj = serializer.deserialize(message.getBody());
        if(obj == null) return;
        Map<String, Object> commands = (Map<String, Object>) obj;

        for(Entry<String, Object> command : commands.entrySet()) {
            try {
                if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_REMOVE)) {
                    socketService.removeConnection((String) command.getValue());
                } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_REPLACE)) {
                    socketService.replaceConnection((String) command.getValue());
                } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_PUT)) {
                    socketService.broadcast(command.getValue());
                } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_UPDATE)) {
                    updatePlayer((Entry<String, Object>) command.getValue());
                } else if(command.getKey().equals(CommonConstants.BOARDCAST_COMMAND)) {
                    socketService.broadcast(command.getValue());
                } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_TURN)) {
                    Map<String, Object> data = (Map<String, Object>) command.getValue();
                    socketService.sendMessage((Integer) data.get(CommonConstants.PLAYER_ID), Map.of(
                        CommonConstants.PLAYER_TURN, data.get(CommonConstants.PLAYER_TURN)
                    ));
                } else if(command.getKey().equals(CommonConstants.PLAYER_COMMAND_WON)) {
                    socketService.broadcast(Map.of(
                        CommonConstants.PLAYER_COMMAND_WON, command.getValue()
                    ));
                }
            } catch (IOException e) {
                log.error("Error occured while excuting {} command", command.getKey());
                e.printStackTrace();
            } catch (PlayerInvalidException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        log.debug(commands.toString());
    }

    private void updatePlayer(Entry<String, Object> playerData) throws IOException {
        int id = Integer.parseInt(playerData.getKey());
        if(gameState.getPlayer1Id() == id) {
            gameState.setPlayer1Name((String) playerData.getValue());
            socketService.broadcast(Map.of(CommonConstants.PLAYER_DATA, Map.of(
                CommonConstants.PLAYER1_NAME, playerData.getValue(),
                CommonConstants.PLAYER2_NAME, gameState.getPlayer2Name()
            )));
        } else if(gameState.getPlayer2Id() == id) {
            gameState.setPlayer2Name((String) playerData.getValue());
            socketService.broadcast(Map.of(CommonConstants.PLAYER_DATA, Map.of(
                CommonConstants.PLAYER1_NAME, gameState.getPlayer1Name(),
                CommonConstants.PLAYER2_NAME, playerData.getValue()
            )));
        } else {
            // TODO: See how to handle viewer name update
        }
    }
}
