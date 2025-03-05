package com.dedshot.game.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class GameBoard implements Serializable{
    private int[][] board;
    public GameBoard() {
        this.board = new int[][] {
            new int[] {0, 0, 0},
            new int[] {0, 0, 0},
            new int[] {0, 0, 0}
        };
    }

    public int[][] set(Integer x, Integer y, Integer value) {
        if(validate(x) && validate(y)) {
            board[x][y] = value;
        }
        return board;
    }

    public int[][] get() { return board; }

    private boolean validate(Integer i) {
        return i >=0 && i < 3;
    }
}
