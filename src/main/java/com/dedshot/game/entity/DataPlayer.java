package com.dedshot.game.entity;

import com.dedshot.game.enums.PlayerTypes;
import lombok.Data;
import lombok.NonNull;

@Data
public class DataPlayer {
    private int id;
    @NonNull private String name;
    private int score;
    private PlayerTypes playerType;

    public DataPlayer(Player player, PlayerTypes type) {
        this.id = player.getId();
        this.name = player.getName();
        this.score = player.getScore();
        this.playerType = type;
    }
}
