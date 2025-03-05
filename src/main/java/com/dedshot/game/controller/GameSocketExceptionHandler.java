package com.dedshot.game.controller;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GameSocketExceptionHandler {
    public void handleException(Exception e) {
        log.error(e.getMessage());
    }
}
