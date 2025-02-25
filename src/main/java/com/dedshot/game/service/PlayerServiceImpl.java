package com.dedshot.game.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.DataPlayer;
import com.dedshot.game.entity.ListEntity;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    private PlayerDAO playerDAO;

    @Override
    public ResponseEntity<String> findAll() throws JsonProcessingException {
        List<DataPlayer> playerList = new ArrayList<>();
        List<Player> playerDAOlist = playerDAO.findAll();
        for(Player player : playerDAOlist) {
            DataPlayer dataPlayer = new DataPlayer(player.getName(), player.getScore(), player.getPlayerType().getPlayerType());
            playerList.add(dataPlayer);
        }

        ListEntity<DataPlayer> list = new ListEntity<>(playerList);
        ObjectMapper om = new ObjectMapper();
        String res = om.writeValueAsString(list);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DataPlayer> findPlayerById(int id) throws PlayerNotFoundException {
        Optional<Player> player = playerDAO.findById(id);
        if(player.isPresent()) {
            DataPlayer dataPlayer = new DataPlayer(player.get().getName(), player.get().getScore(), player.get().getPlayerType().getPlayerType());
            return new ResponseEntity<>(dataPlayer, HttpStatus.OK);
        }
        else throw new PlayerNotFoundException(id);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updatePlayer(String name, HttpSession session) throws PlayerNotFoundException {
        int id = (Integer) session.getAttribute("id");
        if(id == 0) throw new PlayerNotFoundException(0);

        Optional<Player> optionalPlayer = playerDAO.findById(id);
        if(optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            player.setName(name);
            playerDAO.save(player);
            return new ResponseEntity<>("Player name has been updated with " + name, HttpStatus.OK);
        } else {
            throw new PlayerNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deletePlayer(int id) throws PlayerNotFoundException{
        playerDAO.deleteById(id);
        return new ResponseEntity<>("Player " + id + " deleted.", HttpStatus.OK);
    }

}
