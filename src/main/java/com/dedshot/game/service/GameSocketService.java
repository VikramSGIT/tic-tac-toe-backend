package com.dedshot.game.service;

import java.io.IOException;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import com.dedshot.game.errors.PlayerInvalidException;
import com.dedshot.game.errors.PlayerNotFoundException;

public interface GameSocketService {
    public void newConnection(ConcurrentWebSocketSessionDecorator session) throws IOException, PlayerNotFoundException;
    public void removeConnection(ConcurrentWebSocketSessionDecorator session);
    public void removeConnection(String sessionId) throws IOException;
    public void replaceConnection(String sessionId) throws IOException;
    public void handlePlayerBoard(ConcurrentWebSocketSessionDecorator session, Integer x, Integer y) throws IOException;
    public void broadcast(Object data) throws IOException;
    public void sendMessage(int id, Object data) throws IOException, PlayerInvalidException;
}
