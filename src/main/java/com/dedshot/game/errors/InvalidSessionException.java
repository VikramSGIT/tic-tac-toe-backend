package com.dedshot.game.errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidSessionException extends RuntimeException{
    public InvalidSessionException() {
        super("Session is invalid or curropted");
        log.error("Session is invalid or curropted");
    }
}
