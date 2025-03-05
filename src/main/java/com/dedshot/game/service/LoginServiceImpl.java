package com.dedshot.game.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dedshot.game.constants.CommonConstants;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.DataPlayer;
import com.dedshot.game.entity.Player;
import com.dedshot.game.enums.PlayerTypes;
import com.dedshot.game.utils.ServiceUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService{

    private PlayerDAO playerDAO;

    @Override
    @Transactional
    public ResponseEntity<String> addPlayer(String name, HttpSession session) {
        Integer id = (Integer) session.getAttribute(CommonConstants.PLAYER_ID);
        if(id != null) {
            Optional<Player> optionalPlayer = playerDAO.findById(id);
            if(optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                try {
                    ObjectMapper om = new ObjectMapper();
                    return new ResponseEntity<>(om.writeValueAsString(new DataPlayer(player, PlayerTypes.VIEWER)), ServiceUtils.getHeaders(), HttpStatus.OK);
                } catch(Exception e) {
                    log.error("Error while converting player: {} to JSON", id);
                    log.error(e.getMessage());
                    return new ResponseEntity<>("Error occured while processing player", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        Player player = new Player();
        player.setName(name);
        
        
        Player newPlayer = playerDAO.save(player);
        session.setAttribute(CommonConstants.PLAYER_ID, newPlayer.getId());

        try {
            ObjectMapper om = new ObjectMapper();
            return new ResponseEntity<>(om.writeValueAsString(new DataPlayer(player, PlayerTypes.VIEWER)), ServiceUtils.getHeaders(), HttpStatus.OK);
        } catch(Exception e) {
            log.error("Error while converting player: {} to JSON", id);
            log.error(e.getMessage());
            return new ResponseEntity<>("Error occured while processing player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
