package com.dedshot.game.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dedshot.game.errors.PlayerErrorResponse;
import com.dedshot.game.errors.PlayerNotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class PlayerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PlayerErrorResponse> handleException(PlayerNotFoundException e) {
        PlayerErrorResponse res = new PlayerErrorResponse(e.getMessage());
        log.warn(e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerErrorResponse> handleException(Exception e) {
        PlayerErrorResponse res = new PlayerErrorResponse(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
}
