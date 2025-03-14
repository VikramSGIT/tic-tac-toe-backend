package com.dedshot.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.entity.DataPlayer;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.dedshot.game.service.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class PlayerRestController {

    private PlayerService playerService;

    @GetMapping("/players/{playerId}")
    public ResponseEntity<DataPlayer> listPlayer(@PathVariable int playerId) throws PlayerNotFoundException {
        return playerService.findPlayerById(playerId);
    }

    @GetMapping("/players")
    public ResponseEntity<String> listPlayers() throws JsonProcessingException {
        return playerService.findAll();
    }
    
    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @PutMapping("/players")
    public ResponseEntity<DataPlayer> updatePlayer(@RequestBody String name, HttpSession session) throws PlayerNotFoundException {
        log.debug("Requested update for player: {} with {}", session.getAttribute(CommonConstants.PLAYER_ID), name);
        return playerService.updatePlayer(name, session);
    }
}
