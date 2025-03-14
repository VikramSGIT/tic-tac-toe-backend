package com.dedshot.game.service;

public interface RedisPubSubService {
    public void playerMove(Object json);
    public void playerUpdate(int playerId, String playerNewName);
    public void sendData2Player(int playerId, Object data);
    public void removeConnection(String sessionId);
    public void replaceConnection(String sessionId);
    public void broadcast(Object json);
}
