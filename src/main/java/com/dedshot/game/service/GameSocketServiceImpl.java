package com.dedshot.game.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.dao.GameState;
import com.dedshot.game.entity.GameBoard;
import com.dedshot.game.enums.PlayerTypes;
import com.dedshot.game.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameSocketServiceImpl implements GameSocketService{
    
    private final GameState state;
    private final RedisPubSubService pubSubService;
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void newConnection(WebSocketSession session) throws IOException{
        int id = (Integer) session.getAttributes().get(CommonConstants.PLAYER_ID);
        Map<String, Object> message = new HashMap<>();

        if(!state.isPlayer1Off() && id == state.getPlayer1Id()) {
            pubSubService.replaceConnection(state.getPlayer1SessionId());
            state.setPlayer1Id(id);
            state.setPlayer1SessionId(session.getId());
            message.put(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER1);
        } else if(!state.isPlayer2Off() && id == state.getPlayer2Id()) {
            pubSubService.replaceConnection(state.getPlayer2SessionId());
            state.setPlayer2Id(id);
            state.setPlayer2SessionId(session.getId());
            message.put(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER2);
        } else if(state.isPlayer1Off()) {
            state.setPlayer1Id(id);
            state.setPlayer1SessionId(session.getId());
            message.put(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER1);

            state.ifGameInit();
        } else if(state.isPlayer2Off()) {
            state.setPlayer2Id(id);
            state.setPlayer2SessionId(session.getId());
            message.put(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER2);

            state.ifGameInit();
        } else {
            message.put(CommonConstants.PLAYER_TYPE, PlayerTypes.VIEWER);
        }

        session.sendMessage(new TextMessage(ServiceUtils.toJSONString(message)));
        session.sendMessage(new TextMessage(ServiceUtils.toJSONString(state.getBoard())));
        sessions.put(session.getId(), session);
    }

    @Override
    public void removeConnection(WebSocketSession session) {
        int id = (int) session.getAttributes().get(CommonConstants.PLAYER_ID);
        String type = (String) session.getAttributes().get(CommonConstants.SOCKET_REMOVAL_TYPE);

        if(type == null) {
            if(id == state.getPlayer1Id()) {
                state.setPlayer1Off();
                log.debug("Player: {}/{} has been disconnected", session.getAttributes().get(CommonConstants.PLAYER_ID), PlayerTypes.PLAYER1);
            } else if (id == state.getPlayer2Id()) {
                state.setPlayer2Off();
                log.debug("Player: {}/{} has been disconnected", session.getAttributes().get(CommonConstants.PLAYER_ID), PlayerTypes.PLAYER2);
            } else {
                log.debug("Player: {}/{} has been disconnected", session.getAttributes().get(CommonConstants.PLAYER_ID), PlayerTypes.VIEWER);
            }
        }

        sessions.remove(session.getId());
    }


    @Override
    public void removeConnection(String sessionId) throws IOException{
        if(sessions.get(sessionId) == null) return;
        WebSocketSession session = sessions.get(sessionId);
        if(session.isOpen()) session.close();
    }

    @Override
    public void replaceConnection(String sessionId) throws IOException{
        if(sessions.get(sessionId) == null) return;
        WebSocketSession session = sessions.get(sessionId);
        session.getAttributes().put(CommonConstants.SOCKET_REMOVAL_TYPE, CommonConstants.PLAYER_COMMAND_REPLACE);
        if(session.isOpen()) session.close();
    }

    @Override
    public void handlePlayerBoard(WebSocketSession session, Integer x, Integer y) {
        int id = (int) session.getAttributes().get(CommonConstants.PLAYER_ID);
        if(id != state.getPlayer1Id() && id != state.getPlayer2Id()) {
             return;
        }

        if(id == state.getTurn()) {
            return;
        }

        GameBoard board = state.getBoard();
        board.set(x, y, 1);
        state.setGameBoard(board);
        state.flipTurn();

        pubSubService.playerMove(board);
    }

    @Override
    public void broadcast(String message) throws IOException {
        for (WebSocketSession ws : sessions.values()) {
            if (ws.isOpen()) {
                try {
                    ws.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    log.error("Error occured while sending message for playerId: {}", 
                                ws.getAttributes().get(CommonConstants.PLAYER_ID));
                    throw e;
                }
            }
        }
    }



}
