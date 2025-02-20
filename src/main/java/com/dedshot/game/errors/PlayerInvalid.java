package com.dedshot.game.errors;

public class PlayerInvalid extends Exception {
    public PlayerInvalid(String message) {
        super("Invalid player operation. " + message);
    }
}
