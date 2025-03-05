package com.dedshot.game.errors;

public class JWTNotfoundException extends RuntimeException {
    public JWTNotfoundException() {
        super("JWT not found.");
    }
}
