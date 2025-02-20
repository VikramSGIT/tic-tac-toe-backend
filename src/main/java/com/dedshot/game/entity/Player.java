package com.dedshot.game.entity;

import com.dedshot.game.enums.PlayerType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="players")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Player {

    @Id
    // This was added because the name of the sequence generator in postgres is different.
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "players_seq")
    @SequenceGenerator(name = "players_seq", sequenceName = "players_player_id_seq", allocationSize = 1)
    @Column(name="player_id")
    private int id;

    @Column(name="player_name")
    @NonNull private String name;

    @Column(name="player_score")
    private int score;

    @Enumerated(EnumType.STRING)
    @Column(name="player_type")
    @NonNull private PlayerType type;
}
