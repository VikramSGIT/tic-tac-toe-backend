package com.dedshot.game.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.dao.GameState;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.DataPlayer;
import com.dedshot.game.entity.ListEntity;
import com.dedshot.game.entity.Player;
import com.dedshot.game.enums.PlayerTypes;
import com.dedshot.game.errors.PlayerNotFoundException;
import com.dedshot.game.utils.ServiceUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private PlayerDAO playerDAO;
    private GameState state;
    private RedisPubSubService pubSubService;

    @Override
    public ResponseEntity<String> findAll() throws JsonProcessingException {
        List<DataPlayer> playerList = new ArrayList<>();
        List<Player> playerDAOlist = playerDAO.findAll();

        // Reduces DB hit cound
        int player1Id = state.getPlayer1Id();
        int player2Id = state.getPlayer2Id();
        for(Player player : playerDAOlist) {
            PlayerTypes type = PlayerTypes.VIEWER;
            if(player1Id == player.getId()) type = PlayerTypes.PLAYER1;
            else if (player2Id == player.getId()) type = PlayerTypes.PLAYER2;
            playerList.add(new DataPlayer(player, type));
        }

        ListEntity<DataPlayer> list = new ListEntity<>(playerList);
        ObjectMapper om = new ObjectMapper();
        try {
            String res = om.writeValueAsString(list);
            return new ResponseEntity<>(res, ServiceUtils.getHeaders(), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("Error while converting to JSON.");
            throw e;
        }
    }

    @Override
    public ResponseEntity<DataPlayer> findPlayerById(int id) throws PlayerNotFoundException {
        Optional<Player> optionalPlayer = playerDAO.findById(id);
        if(optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();

            return new ResponseEntity<>(new DataPlayer(player, state.ifPlayerType(id)), ServiceUtils.getHeaders(), HttpStatus.OK);
        }
        else throw new PlayerNotFoundException(id);
    }

    @Override
    @Transactional
    public ResponseEntity<DataPlayer> updatePlayer(String name, HttpSession session) throws PlayerNotFoundException {
        Integer id = (Integer) session.getAttribute(CommonConstants.PLAYER_ID);
        if(id == null || id == 0) throw new PlayerNotFoundException(0);

        Optional<Player> optionalPlayer = playerDAO.findById(id);
        if(optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            player.setName(name);
            Player newPlayer = playerDAO.save(player);
            
            pubSubService.playerUpdate(newPlayer.getId(), newPlayer.getName());
            return new ResponseEntity<>(new DataPlayer(newPlayer, state.ifPlayerType(newPlayer.getId())), ServiceUtils.getHeaders(), HttpStatus.OK);
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
