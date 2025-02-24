package com.dedshot.game.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.ListEntity;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerInvalidException;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    private PlayerDAO playerDAO;

    @Override
    public ResponseEntity<String> findAll() throws JsonProcessingException {

        ListEntity<Player> list = new ListEntity<>(playerDAO.findAll());
        ObjectMapper om = new ObjectMapper();
    
        String res = om.writeValueAsString(list);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    public Player findPlayerById(int id) throws PlayerNotFoundException {
        Optional<Player> player = playerDAO.findById(id);
        if(player.isPresent()) return player.get();
        else throw new PlayerNotFoundException(id);
    }

    @Override
    @Transactional
    public Player addPlayer(Player player) throws PlayerInvalidException {
        if(player.getId() != 0) throw new PlayerInvalidException(null);
        
        return playerDAO.save(player);
    }

    @Override
    @Transactional
    public Player updatePlayer(Player player) throws PlayerNotFoundException {
        if(player.getId() == 0) throw new PlayerNotFoundException(0);
        return playerDAO.save(player);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deletePlayer(int id) throws PlayerNotFoundException{
        playerDAO.delete(findPlayerById(id));
        return new ResponseEntity<>("Player " + id + " deleted.", HttpStatus.OK);
    }

}
