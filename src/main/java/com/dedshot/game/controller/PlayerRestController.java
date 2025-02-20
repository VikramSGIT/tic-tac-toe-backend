package com.dedshot.game.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerInvalid;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.dedshot.game.service.PlayerService;
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
    public Player listPlayer(@PathVariable int playerId) throws PlayerNotFoundException {
        return playerService.findPlayerById(playerId);
    }

    @GetMapping("/players")
    public List<Player> listPlayers() {
        return playerService.findAll();
    }

    @PostMapping("/players")
    public Player addPlayer(@RequestBody Player player) throws PlayerInvalid {
        return playerService.addPlayer(player);
    }
    
    @PutMapping("/players")
    public Player updatePlayer(@RequestBody Player player) throws PlayerNotFoundException {
        return playerService.updatePlayer(player);
    }
    
}
