package com.dedshot.game.service;

import java.io.IOException;
import org.springframework.web.socket.WebSocketSession;

public interface GameSocketService {
    public void newConnection(WebSocketSession session) throws IOException;
    public void removeConnection(WebSocketSession session);
    public void removeConnection(String sessionId) throws IOException;
    public void replaceConnection(String sessionId) throws IOException;
    public void handlePlayerBoard(WebSocketSession session, Integer x, Integer y);
    public void broadcast(String message) throws IOException;
}
