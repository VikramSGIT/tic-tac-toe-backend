package com.dedshot.game.entity;

import java.io.Serializable;

import com.dedshot.game.enums.PlayerTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRedis implements Serializable {
    private PlayerTypes playerType;
}
