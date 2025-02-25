package com.dedshot.game.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.DataPlayer;
import com.dedshot.game.entity.Player;
import com.dedshot.game.entity.PlayerType;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService{

    private PlayerDAO playerDAO;

    @Override
    @Transactional
    public ResponseEntity<DataPlayer> addPlayer(String name, HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if(id != null) {
            Optional<Player> optionalPlayer = playerDAO.findById(id);
            if(optionalPlayer.isPresent()) {
                Player player = optionalPlayer.get();
                DataPlayer dataPlayer = new DataPlayer(player.getId(), player.getName(), player.getScore(), player.getPlayerType());
                return new ResponseEntity<>(dataPlayer, HttpStatus.OK);
            }
        }
        PlayerType playerType = new PlayerType();
        playerType.setPlayerType(com.dedshot.game.enums.PlayerType.VIEWER);

        Player player = new Player();
        player.setName(name);
        player.setPlayerTypeEntity(playerType);

        playerType.setPlayer(player);
        
        Player newPlayer = playerDAO.save(player);
        session.setAttribute("id", newPlayer.getId());

        DataPlayer dataPlayer = new DataPlayer(newPlayer.getId(), newPlayer.getName(), newPlayer.getScore(), newPlayer.getPlayerType());

        return new ResponseEntity<>(dataPlayer, HttpStatus.OK);
    }
    
}
