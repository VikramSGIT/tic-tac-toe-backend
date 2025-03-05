package com.dedshot.game.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerCommand {
    private String command;
    private String[] values;
}
