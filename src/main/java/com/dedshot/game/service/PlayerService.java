package com.dedshot.game.service;

import org.springframework.http.ResponseEntity;

import com.dedshot.game.entity.DataPlayer;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpSession;

public interface PlayerService {
    public ResponseEntity<String> findAll() throws JsonProcessingException;
    public ResponseEntity<DataPlayer> findPlayerById(int id) throws PlayerNotFoundException;
    public ResponseEntity<DataPlayer> updatePlayer(String name, HttpSession session) throws PlayerNotFoundException;
    public ResponseEntity<String> deletePlayer(int id) throws PlayerNotFoundException;
}
