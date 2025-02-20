package com.dedshot.game.errors;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException(int id) {
        super("Player: " + id + " not found.");
    }
}
