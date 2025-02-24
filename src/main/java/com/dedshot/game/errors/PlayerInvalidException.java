package com.dedshot.game.errors;

public class PlayerInvalidException extends Exception {
    public PlayerInvalidException(String message) {
        super("Invalid player operation. " + message);
    }
}
