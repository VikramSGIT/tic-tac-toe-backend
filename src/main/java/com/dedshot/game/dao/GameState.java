package com.dedshot.game.dao;

import org.springframework.data.redis.core.ValueOperations;
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

    public int getPlayer1Id() {
        return (int) verify(redis.get(CommonConstants.PLAYER1));
    }

    public String getPlayer1SessionId() {
        return (String) verify(redis.get(CommonConstants.PLAYER1_SESSION));
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
        redis.set(CommonConstants.PLAYER1, CommonConstants.PLAYER_OFFLINE);
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

    public int setPlayer2Id(int id) {
        set(CommonConstants.PLAYER2, id);
        return id;
    }

    public String setPlayer2SessionId(String sessionId) {
        set(CommonConstants.PLAYER2_SESSION, sessionId);
        return sessionId;
    }

    public void setPlayer2Off() {
        redis.set(CommonConstants.PLAYER2, CommonConstants.PLAYER_OFFLINE);
    }

    public boolean isPlayer2Off() {
        Integer res = (Integer) redis.get(CommonConstants.PLAYER2);
        if(res == null) return true;
        return res == CommonConstants.PLAYER_OFFLINE;
    }



    public int getTurn() {
        return (int) verify(redis.get(CommonConstants.TURN));
    }

    public int setTurn(int id) {
        set(CommonConstants.TURN, id);
        return id;
    }

    public int flipTurn() {
        return setTurn(getTurn() == getPlayer1Id() ? getPlayer2Id() : getPlayer1Id());
    }

    public boolean isTurnSet() {return redis.get(CommonConstants.TURN) != null; }


    public GameBoard getBoard() {
        return (GameBoard) verify(redis.get(CommonConstants.GAME_BOARD));
    }

    public GameBoard setGameBoard(GameBoard board) {
        set(CommonConstants.GAME_BOARD, board);
        return board;
    }

    public boolean isGameBoardSet() { return redis.get(CommonConstants.GAME_BOARD) != null; }


    public void ifGameInit() {
        if(!isTurnSet()) setTurn(getPlayer1Id());
        if(!isGameBoardSet()) setGameBoard(new GameBoard());
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

    private Object verify(Object val) {
        if(val == null) throw new RedisDataCorruptedException();
        return val;
    }
}
