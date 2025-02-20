package com.dedshot.game.dao;

import java.util.List;

import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerNotFoundException;

public interface PlayerDAO {
    List<Player> findAll();
    Player findById(int id) throws PlayerNotFoundException;
    Player save(Player player);
    void delete(int id) throws PlayerNotFoundException;
}
