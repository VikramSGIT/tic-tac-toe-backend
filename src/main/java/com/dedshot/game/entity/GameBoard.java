package com.dedshot.game.entity;

import java.io.Serializable;
import com.dedshot.game.enums.PlayerTypes;

import lombok.Data;

@Data
public class GameBoard implements Serializable{
    private PlayerTypes[][] board;

    public static GameBoard newGameBoard() { return new GameBoard(); }
    public GameBoard() {
        this.board = new PlayerTypes[][] {
            new PlayerTypes[] {null, null, null},
            new PlayerTypes[] {null, null, null},
            new PlayerTypes[] {null, null, null}
        };
    }

    public PlayerTypes[][] set(int x, int y, PlayerTypes value) {
        if(validate(x, y)) {
            board[x][y] = value;
        } else {
            return null;
        }
        return board;
    }

    public PlayerTypes[][] get() { return board; }

    private boolean validate(int x, int y) {
        if(x < 0 || x > 2) return false;
        if(y < 0 || y > 2) return false;
        if(board[x][y] != null) return false;
        return true;
    }
}
