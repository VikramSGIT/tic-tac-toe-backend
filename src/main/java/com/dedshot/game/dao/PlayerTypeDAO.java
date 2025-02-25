package com.dedshot.game.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dedshot.game.entity.PlayerType;

public interface PlayerTypeDAO extends JpaRepository<PlayerType, Integer> {}
