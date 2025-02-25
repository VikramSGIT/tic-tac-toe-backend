package com.dedshot.game.entity;

import com.dedshot.game.enums.PlayerType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DataPlayer {
    private String name;
    private int score;
    private PlayerType playerType;
}
