package com.dedshot.game.errors;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException() {
        super("Session not found!");
    }
}
