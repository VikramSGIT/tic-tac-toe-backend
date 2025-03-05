package com.dedshot.game.errors;

public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException() {
        super("The provided JWT is invalid or currupted.");
    }
}
