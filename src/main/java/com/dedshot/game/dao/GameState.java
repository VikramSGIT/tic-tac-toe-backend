package com.dedshot.game.dao;

import java.util.Objects;
import java.util.Set;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.entity.GameBoard;
import com.dedshot.game.enums.PlayerTypes;
import com.dedshot.game.errors.RedisDataCorruptedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class GameState {
    private ValueOperations<String, Object> redis;
    private ZSetOperations<String, Object> redisZSet;

    public int getPlayer1Id() {
        return (int) verify(redis.get(CommonConstants.PLAYER1));
    }

    public String getPlayer1SessionId() {
        return (String) verify(redis.get(CommonConstants.PLAYER1_SESSION));
    }

    public String getPlayer1Name() {
        String i = (String) redis.get(CommonConstants.PLAYER1_NAME);
        if(Objects.isNull(i)) return CommonConstants.PLAYER_OFFLINE_NAME;
        return i;
    }

    public String setPlayer1Name(String name) {
        set(CommonConstants.PLAYER1_NAME, name);
        return name;
    }

    public int setPlayer1Id(int id) {
        set(CommonConstants.PLAYER1, id);
        return id;
    }

    public String setPlayer1SessionId(String sessionId) {
        set(CommonConstants.PLAYER1_SESSION, sessionId);
        return sessionId;
    }

    public void setPlayer1Off() {
        setPlayer1Id(CommonConstants.PLAYER_OFFLINE);
        setPlayer1Name(CommonConstants.PLAYER_OFFLINE_NAME);
        setPlayer1SessionId(CommonConstants.PLAYER_OFFLINE_NAME);
    }

    public boolean isPlayer1Off() {
        Integer res = (Integer) redis.get(CommonConstants.PLAYER1);
        if(res == null) return true;
        return res == CommonConstants.PLAYER_OFFLINE;
    }



    public int getPlayer2Id() {
        return (int) verify(redis.get(CommonConstants.PLAYER2));
    }


    public String getPlayer2SessionId() {
        return (String) verify(redis.get(CommonConstants.PLAYER2_SESSION));
    }

    public String getPlayer2Name() {
        String i = (String) redis.get(CommonConstants.PLAYER2_NAME);
        if(Objects.isNull(i)) return CommonConstants.PLAYER_OFFLINE_NAME;
        return i;
    }

    public String setPlayer2Name(String name) {
        set(CommonConstants.PLAYER2_NAME, name);
        return name;
    }

    public int setPlayer2Id(int id) {
        set(CommonConstants.PLAYER2, id);
        return id;
    }

    public String setPlayer2SessionId(String sessionId) {
        set(CommonConstants.PLAYER2_SESSION, sessionId);
        return sessionId;
    }

    public void setPlayer2Off() {
        setPlayer2Id(CommonConstants.PLAYER_OFFLINE);
        setPlayer2Name(CommonConstants.PLAYER_OFFLINE_NAME);
        setPlayer2SessionId(CommonConstants.PLAYER_OFFLINE_NAME);
    }

    public boolean isPlayer2Off() {
        Integer res = (Integer) redis.get(CommonConstants.PLAYER2);
        if(res == null) return true;
        return res == CommonConstants.PLAYER_OFFLINE;
    }



    public PlayerTypes getTurn() {
        return (PlayerTypes) verify(redis.get(CommonConstants.TURN));
    }

    public PlayerTypes setTurn(PlayerTypes player) {
        set(CommonConstants.TURN, player);
        return player;
    }

    public PlayerTypes flipTurn() {
        return setTurn(Objects.equals(getTurn(), PlayerTypes.PLAYER1) ? PlayerTypes.PLAYER2 : PlayerTypes.PLAYER1);
    }

    public boolean isTurnSet() {return redis.get(CommonConstants.TURN) != null; }


    public GameBoard getBoard() {
        return (GameBoard) get(CommonConstants.GAME_BOARD);
    }

    public GameBoard setGameBoard(GameBoard board) {
        set(CommonConstants.GAME_BOARD, board);
        return board;
    }

    public boolean isGameBoardSet() { return redis.get(CommonConstants.GAME_BOARD) != null; }


    public void ifGameInit() {
        if(!isTurnSet()) setTurn(PlayerTypes.PLAYER1);
        if(!isGameBoardSet()) setGameBoard(GameBoard.newGameBoard());
    }

    public String addPlayerSession(String sessionId) {
        redisZSet.add(CommonConstants.SOCKET_SESSION_ID, sessionId, System.currentTimeMillis());
        return sessionId;
    }

    public void removePlayerSession(String sessionId) {
        redisZSet.remove(CommonConstants.SOCKET_SESSION_ID, sessionId);
    }


    public void set(String id, Object value) {
        redis.set(id, value);
    }

    public Object get(Object val) {
        return verify(redis.get(val));
    }

    public PlayerTypes ifPlayerType(int playerId) {
        if(getPlayer1Id() == playerId) return PlayerTypes.PLAYER1;
        else if (getPlayer2Id() == playerId) return PlayerTypes.PLAYER2;
        return PlayerTypes.VIEWER;
    }

    public String popNextPlayerSession() {
        Set<TypedTuple<Object>> players = redisZSet.popMin(CommonConstants.SOCKET_SESSION_ID, 1);
        if(players == null || players.isEmpty()) return null;
        return (String) players.iterator().next().getValue();
    }

    private Object verify(Object val) {
        if(val == null) throw new RedisDataCorruptedException();
        return val;
    }
}
