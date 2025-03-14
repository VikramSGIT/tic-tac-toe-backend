package com.dedshot.game.controller;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import com.dedshot.game.errors.PlayerNotFoundException;
import com.dedshot.game.service.GameSocketService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GameSocketController {
    private GameSocketService gameSocketService;

    public void newPlayerConnected(@NonNull WebSocketSession session) throws IOException, PlayerNotFoundException {
        gameSocketService.newConnection(new ConcurrentWebSocketSessionDecorator(session, 2000, 4096));
    }

    public void playerDisconnected(@NonNull WebSocketSession session) {
        gameSocketService.removeConnection(new ConcurrentWebSocketSessionDecorator(session, 2000, 4096));
    }

    public void playerMove(@NonNull WebSocketSession session, Integer x, Integer y) throws IOException {
        gameSocketService.handlePlayerBoard(new ConcurrentWebSocketSessionDecorator(session, 2000, 4096), x, y);
    }
}
