package com.dedshot.game.service;

import com.dedshot.game.enums.PlayerTypes;

public interface RedisPubSubService {
    public void playerMove(Object json);
    public void playerUpdate(int playerId, String playerNewName);
    public void sendTrun(int playerId, Object data);
    public void removeConnection(String sessionId);
    public void replaceConnection(String sessionId);
    public void broadcast(Object json);
    public void playerWon(PlayerTypes playerType, int score1, int score2);
    public void updatePlayerType(String socketSessionId, PlayerTypes playerType);
}
