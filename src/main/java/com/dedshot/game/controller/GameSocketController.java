package com.dedshot.game.controller;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import com.dedshot.game.service.GameSocketService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GameSocketController {
    private GameSocketService gameSocketService;

    public void newPlayerConnected(@NonNull WebSocketSession session) throws Exception {
        gameSocketService.newConnection(session);
    }

    public void playerDisconnected(@NonNull WebSocketSession session) throws Exception {
        gameSocketService.removeConnection(session);
    }

    public void playerMove(@NonNull WebSocketSession session, Integer x, Integer y) throws Exception {
        gameSocketService.handlePlayerBoard(session, x, y);
    }
}
