package com.dedshot.game.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import com.dedshot.game.constants.CommonConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RedisPubSubServiceImpl implements RedisPubSubService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Override
    public void playerMove(Object gameBoard) {
        Map<String, Object> command = new HashMap<>();
        command.put(CommonConstants.PLAYER_COMMAND_PUT, gameBoard);
        redisTemplate.convertAndSend(topic.getTopic(), command);
    }

    @Override
    public void playerUpdate(int playerId, String playerNewName) {
        Map<String, Object> command = new HashMap<>();
        Map<Integer, String> player = new HashMap<>();
        player.put(playerId, playerNewName);
        command.put(CommonConstants.PLAYER_COMMAND_UPDATE, player);
        redisTemplate.convertAndSend(topic.getTopic(), command);
    }

    @Override
    public void removeConnection(String sessionId) {
        Map<String, Object> command = new HashMap<>();
        command.put(CommonConstants.PLAYER_COMMAND_REMOVE, sessionId);
        redisTemplate.convertAndSend(topic.getTopic(), command);
    }

    @Override
    public void replaceConnection(String sessionId) {
        Map<String, Object> command = new HashMap<>();
        command.put(CommonConstants.PLAYER_COMMAND_REPLACE, sessionId);
        redisTemplate.convertAndSend(topic.getTopic(), command);
    }

}
