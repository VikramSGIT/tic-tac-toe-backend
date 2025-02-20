package com.dedshot.game.service;

import java.util.List;

import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerInvalid;
import com.dedshot.game.errors.PlayerNotFoundException;

public interface PlayerService {
    public List<Player> findAll();
    public Player findPlayerById(int id) throws PlayerNotFoundException;
    public Player addPlayer(Player player)  throws PlayerInvalid;
    public Player updatePlayer(Player player) throws PlayerNotFoundException;
    public void deletePlayer(int id) throws PlayerNotFoundException;
}
