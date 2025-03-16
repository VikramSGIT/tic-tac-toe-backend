package com.dedshot.game.service;

import java.io.IOException;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import com.dedshot.game.enums.PlayerTypes;
import com.dedshot.game.errors.PlayerInvalidException;
import com.dedshot.game.errors.PlayerNotFoundException;

public interface GameSocketService {
    void newConnection(ConcurrentWebSocketSessionDecorator session) throws IOException, PlayerNotFoundException;
    void removeConnection(ConcurrentWebSocketSessionDecorator session);
    void removeConnection(String sessionId) throws IOException;
    void replaceConnection(String sessionId) throws IOException;
    void handlePlayerBoard(ConcurrentWebSocketSessionDecorator session, Integer x, Integer y) throws IOException, PlayerInvalidException;
    void broadcast(Object data) throws IOException;
    void sendMessage(int id, Object data) throws IOException, PlayerInvalidException;
    void ifSetPlayerType(String socketSessionId, PlayerTypes playerType) throws PlayerInvalidException, PlayerNotFoundException, IOException;
}
