package com.dedshot.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/crm")
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
    
    @PutMapping("/players")
    public ResponseEntity<String> updatePlayer(@RequestBody String name, HttpSession session) throws PlayerNotFoundException {
        return playerService.updatePlayer(name, session);
    }
}
