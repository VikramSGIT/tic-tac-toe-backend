package com.dedshot.game.service;

import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.enums.PlayerTypes;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisPubSubServiceImpl implements RedisPubSubService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Override
    public void playerMove(Object gameBoard) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(CommonConstants.PLAYER_COMMAND_PUT, gameBoard));
    }

    @Override
    public void playerUpdate(int playerId, String playerNewName) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(
                CommonConstants.PLAYER_COMMAND_UPDATE, new SimpleEntry<String, String>(
                    Integer.toString(playerId), playerNewName
        )));
    }

    @Override
    public void removeConnection(String sessionId) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(CommonConstants.PLAYER_COMMAND_REMOVE, sessionId));
    }

    @Override
    public void replaceConnection(String sessionId) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(CommonConstants.PLAYER_COMMAND_REPLACE, sessionId));
    }

    @Override
    public void broadcast(Object json) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(CommonConstants.BOARDCAST_COMMAND, json));
    }

    @Override
    public void sendTrun(int playerId, Object data) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(
            CommonConstants.PLAYER_COMMAND_TURN, Map.of(
                CommonConstants.PLAYER_ID, playerId,
                CommonConstants.PLAYER_TURN, data
            )
        ));
    }

    @Override
    public void playerWon(PlayerTypes playerType, int score1, int score2) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(
            CommonConstants.PLAYER_COMMAND_WON, Map.of(
                CommonConstants.PLAYER_TYPE, playerType,
                CommonConstants.PLAYER1_SCORE, score1,
                CommonConstants.PLAYER2_SCORE, score2
        )));
    }

    @Override
    public void updatePlayerType(String socketSessionId, PlayerTypes playerType) {
        redisTemplate.convertAndSend(topic.getTopic(), Map.of(
            CommonConstants.PLAYER_COMMAND_TYPE_UPDATE, Map.of(
                CommonConstants.SOCKET_SESSION_ID, socketSessionId,
                CommonConstants.PLAYER_TYPE, playerType
        )));
    }
}
