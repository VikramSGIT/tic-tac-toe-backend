package com.dedshot.game.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerInvalidException;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.dedshot.game.service.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/crm")
@AllArgsConstructor
@Slf4j
public class PlayerRestController {

    private PlayerService playerService;

    @GetMapping("/players/{playerId}")
    public ResponseEntity<Player> listPlayer(@PathVariable int playerId) throws PlayerNotFoundException {
        return new ResponseEntity<>(playerService.findPlayerById(playerId), HttpStatus.OK);
    }

    @GetMapping("/players")
    public ResponseEntity<String> listPlayers() throws JsonProcessingException {
        return playerService.findAll();
    }

    @PostMapping("/players")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) throws PlayerInvalidException {
        return new ResponseEntity<>(playerService.addPlayer(player), HttpStatus.OK);
    }
    
    @PutMapping("/players")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player player) throws PlayerNotFoundException {
        return new ResponseEntity<>(playerService.updatePlayer(player), HttpStatus.OK);
    }
    
    @DeleteMapping("/players/{playerId}")
    public ResponseEntity<String> deletePlayer(@PathVariable int playerId) throws PlayerNotFoundException {
        return playerService.deletePlayer(playerId);
    }
}
