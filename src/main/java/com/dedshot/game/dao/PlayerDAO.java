package com.dedshot.game.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dedshot.game.entity.Player;

public interface PlayerDAO extends JpaRepository<Player, Integer> {}
