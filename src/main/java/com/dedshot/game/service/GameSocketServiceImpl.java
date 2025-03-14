package com.dedshot.game.service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.dao.GameState;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.GameBoard;
import com.dedshot.game.entity.Player;
import com.dedshot.game.enums.PlayerTypes;
import com.dedshot.game.errors.PlayerInvalidException;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.dedshot.game.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameSocketServiceImpl implements GameSocketService{

    private final PlayerDAO playerDAO;
    private final GameState state;
    private final RedisPubSubService pubSubService;
    private final Map<String, ConcurrentWebSocketSessionDecorator> sessions = new ConcurrentHashMap<>();

    @Override
    public void newConnection(ConcurrentWebSocketSessionDecorator session) throws IOException, PlayerNotFoundException {
        sessions.put(session.getId(), session);
        int id = (Integer) session.getAttributes().get(CommonConstants.PLAYER_ID);

        if(!state.isPlayer1Off() && id == state.getPlayer1Id()) {
            pubSubService.replaceConnection(state.getPlayer1SessionId());
            state.setPlayer1Id(id);
            state.setPlayer1SessionId(session.getId());

            sendMessage(session, Map.of(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER1));
            sendMessage(session, Map.of(CommonConstants.PLAYER_TURN, state.getTurn()));
            sendMessage(session, Map.of(
                CommonConstants.PLAYER_DATA, Map.of(
                    CommonConstants.PLAYER1_NAME, state.getPlayer1Name(),
                    CommonConstants.PLAYER2_NAME, state.getPlayer2Name()
                )
            ));
        } else if(!state.isPlayer2Off() && id == state.getPlayer2Id()) {
            pubSubService.replaceConnection(state.getPlayer2SessionId());
            state.setPlayer2Id(id);
            state.setPlayer2SessionId(session.getId());

            sendMessage(session, Map.of(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER2));
            sendMessage(session, Map.of(CommonConstants.PLAYER_TURN, state.getTurn()));
            sendMessage(session, Map.of(
                CommonConstants.PLAYER_DATA, Map.of(
                    CommonConstants.PLAYER1_NAME, state.getPlayer1Name(),
                    CommonConstants.PLAYER2_NAME, state.getPlayer2Name()
                )
            ));
        } else if(state.isPlayer1Off()) {
            state.setPlayer1Id(id);
            state.setPlayer1SessionId(session.getId());

            Optional<Player> optionalPlayer = playerDAO.findById(id);
            if(optionalPlayer.isEmpty()) throw new PlayerNotFoundException(id);

            state.setPlayer1Name(optionalPlayer.get().getName());
            state.ifGameInit();
            sendMessage(session, Map.of(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER1));
            sendMessage(session, Map.of(CommonConstants.PLAYER_TURN, state.getTurn()));
            pubSubService.broadcast(Map.of(
                CommonConstants.PLAYER_DATA, Map.of(
                    CommonConstants.PLAYER1_NAME, optionalPlayer.get().getName(),
                    CommonConstants.PLAYER2_NAME, state.getPlayer2Name()
                )
            ));
        } else if(state.isPlayer2Off()) {
            state.setPlayer2Id(id);
            state.setPlayer2SessionId(session.getId());

            Optional<Player> optionalPlayer = playerDAO.findById(id);
            if(optionalPlayer.isEmpty()) throw new PlayerNotFoundException(id);

            state.setPlayer2Name(optionalPlayer.get().getName());
            state.ifGameInit();
            sendMessage(session, Map.of(CommonConstants.PLAYER_TYPE, PlayerTypes.PLAYER2));
            sendMessage(session, Map.of(CommonConstants.PLAYER_TURN, state.getTurn()));
            pubSubService.broadcast(Map.of(
                CommonConstants.PLAYER_DATA, Map.of(
                    CommonConstants.PLAYER1_NAME, state.getPlayer1Name(),
                    CommonConstants.PLAYER2_NAME, optionalPlayer.get().getName()
                )
            ));
        } else {
            sendMessage(session, Map.of(CommonConstants.PLAYER_TYPE, PlayerTypes.VIEWER));
            sendMessage(session, Map.of(
                CommonConstants.PLAYER_DATA, Map.of(
                    CommonConstants.PLAYER1_NAME, state.getPlayer1Name(),
                    CommonConstants.PLAYER2_NAME, state.getPlayer2Name()
                )
            ));
        }

        sendMessage(session, Map.of(CommonConstants.GAME_BOARD, state.getBoard().getBoard()));
    }

    @Override
    public void removeConnection(ConcurrentWebSocketSessionDecorator session) {
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
        ConcurrentWebSocketSessionDecorator session = sessions.get(sessionId);
        sendClosingMessage(session, "Closing connection as a new connection was opened by the same player.");
        session.getAttributes().put(CommonConstants.SOCKET_REMOVAL_TYPE, CommonConstants.PLAYER_COMMAND_REPLACE);
        if(session.isOpen()) session.close();
    }

    @Override
    public void handlePlayerBoard(ConcurrentWebSocketSessionDecorator session, Integer x, Integer y) throws IOException{
        int id = (int) session.getAttributes().get(CommonConstants.PLAYER_ID);
        int player1Id = state.getPlayer1Id();
        int player2Id = state.getPlayer2Id();
        PlayerTypes turn = state.getTurn();

        if((id == player1Id && Objects.equals(turn, PlayerTypes.PLAYER1)) || 
            (id == player2Id && Objects.equals(turn, PlayerTypes.PLAYER2))) {
            GameBoard board = state.getBoard();
            if(board.set(x, y, turn) == null) {
                sendInvalidMoveMessage(session);
                return;
            }
            state.setGameBoard(board);
    
            state.flipTurn();
            
            pubSubService.playerMove(Map.of(CommonConstants.GAME_BOARD, board.getBoard()));

            turn = state.getTurn();
            pubSubService.sendData2Player(player1Id, turn);
            pubSubService.sendData2Player(player2Id, turn);
        } else {
            sendInvalidMoveMessage(session);
        }
    }

    @Override
    public void broadcast(Object data) throws IOException {
        for (ConcurrentWebSocketSessionDecorator ws : sessions.values()) {
            if (ws.isOpen()) {
                sendMessage(ws, data);
                log.debug("Broadcast message sent to {} with {}", ws.getId(), data);
            }
        }
    }

    @Override
    public void sendMessage(int id, Object data) throws IOException, PlayerInvalidException {
        for(ConcurrentWebSocketSessionDecorator ws : sessions.values()) {
            if(ws.isOpen()) {
                Integer playerId = (Integer) ws.getAttributes().get(CommonConstants.PLAYER_ID);
                if(playerId == null) throw new PlayerInvalidException();
                if(Objects.equals(playerId, id)) {
                    sendMessage(ws, data);
                    log.debug("Broadcast message sent to {} with {}", ws.getId(), data);
                }
            }
        }
    }

    private void sendMessage(ConcurrentWebSocketSessionDecorator ws, Object message) throws IOException {
        ws.sendMessage(new TextMessage(ServiceUtils.toJSONString(Map.of(
            CommonConstants.SOCKET_STATUS_CODE, "200",
            CommonConstants.SOCKET_MESSAGE, message
        ))));
    }

    private void sendClosingMessage(ConcurrentWebSocketSessionDecorator ws, Object message) throws IOException {
        ws.sendMessage(new TextMessage(ServiceUtils.toJSONString(Map.of(
            CommonConstants.SOCKET_STATUS_CODE, "499", // Means close the connection.
            CommonConstants.SOCKET_MESSAGE, message
        ))));
    }

    private void sendInvalidMoveMessage(ConcurrentWebSocketSessionDecorator ws) throws IOException {
        ws.sendMessage(new TextMessage(ServiceUtils.toJSONString(Map.of(
            CommonConstants.SOCKET_STATUS_CODE, "400",
            CommonConstants.SOCKET_MESSAGE, "Invalid move"
        ))));
    }
}
