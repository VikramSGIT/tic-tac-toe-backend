package com.dedshot.game.service;

import org.springframework.http.ResponseEntity;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerInvalidException;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PlayerService {
    public ResponseEntity<String> findAll() throws JsonProcessingException;
    public Player findPlayerById(int id) throws PlayerNotFoundException;
    public Player addPlayer(Player player) throws PlayerInvalidException;
    public Player updatePlayer(Player player) throws PlayerNotFoundException;
    public ResponseEntity<String> deletePlayer(int id) throws PlayerNotFoundException;
}
