package com.dedshot.game.entity;

import java.io.Serializable;
import java.util.Objects;
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

    public boolean checkStatus(PlayerTypes player) {
        //Check row for win
        for (int i = 0; i < 3; i++) {
            if (Objects.equals(board[i][0], player) && Objects.equals(board[i][1], player) && Objects.equals(board[i][2], player)) {
                return true;
            }
        }
    
        // Check columns for win
        for (int j = 0; j < 3; j++) {
            if (Objects.equals(board[0][j], player) && Objects.equals(board[1][j], player) && Objects.equals(board[2][j], player)) {
                return true;
            }
        }
    
        // Check main diagonal (top-left to bottom-right)
        if (Objects.equals(board[0][0], player) && Objects.equals(board[1][1], player) && Objects.equals(board[2][2], player)) {
            return true;
        }
    
        // Check anti-diagonal (top-right to bottom-left)
        if (Objects.equals(board[0][2], player) && Objects.equals(board[1][1], player) && Objects.equals(board[2][0], player)) {
            return true;
        }
    
        // If no win condition is met, return false
        return false;
    }

    public PlayerTypes[][] get() { return board; }

    private boolean validate(int x, int y) {
        if((x < 0 || x > 2) || (y < 0 || y > 2) || (board[x][y] != null)) return false;
        return true;
    }
}
