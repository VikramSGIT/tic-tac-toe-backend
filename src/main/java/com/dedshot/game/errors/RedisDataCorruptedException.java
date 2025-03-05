package com.dedshot.game.errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisDataCorruptedException extends RuntimeException{
    public RedisDataCorruptedException() {
        super("Data on redis has been corrupted.");
    }

    public RedisDataCorruptedException(Exception e) {
        super("Data on redis has been corrupted.");
        log.error(e.getMessage());
    }
}
