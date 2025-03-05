package com.dedshot.game.errors;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerInvalidException extends Exception {
    public PlayerInvalidException(String message) {
        super("Invalid player operation. " + message);
    }
}
