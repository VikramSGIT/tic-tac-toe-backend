package com.dedshot.game.entity;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "player_types")
public class PlayerType {
    @Id
    @Column(name = "player_id", nullable = false)
    private int id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NonNull private com.dedshot.game.enums.PlayerType playerType;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id", referencedColumnName = "player_id")
    private Player player;
}
