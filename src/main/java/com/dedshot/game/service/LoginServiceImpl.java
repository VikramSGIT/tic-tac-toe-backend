package com.dedshot.game.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dedshot.game.dao.PlayerDAO;
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
    public ResponseEntity<String> addPlayer(String name, HttpSession session) {
        Integer id = (Integer) session.getAttribute("id");
        if(id != null) {
            Optional<Player> player = playerDAO.findById(id);
            if(player.isPresent()) return new ResponseEntity<>(player.get().getPlayerType().getPlayerType().toString(), HttpStatus.OK);
        }
        PlayerType playerType = new PlayerType();
        playerType.setPlayerType(com.dedshot.game.enums.PlayerType.VIEWER);

        Player player = new Player();
        player.setName(name);
        player.setPlayerType(playerType);

        playerType.setPlayer(player);
        
        Player newPlayer = playerDAO.save(player);
        session.setAttribute("id", newPlayer.getId());

        return new ResponseEntity<>(newPlayer.getPlayerType().getPlayerType().toString(), HttpStatus.OK);
    }
    
}
