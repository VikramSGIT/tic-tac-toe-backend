package com.dedshot.game.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.dedshot.game.dao.PlayerDAO;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerInvalid;
import com.dedshot.game.errors.PlayerNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    private PlayerDAO playerDAO;

    @Override
    public List<Player> findAll() {
        return playerDAO.findAll();
    }

    @Override
    public Player findPlayerById(int id) throws PlayerNotFoundException {
        try {
            return playerDAO.findById(id);
        } catch (Exception e) {
            throw new PlayerNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public Player addPlayer(Player player) throws PlayerInvalid {
        if(player.getId() != 0) throw new PlayerInvalid(null);
        
        return playerDAO.save(player);
    }

    @Override
    @Transactional
    public Player updatePlayer(Player player) throws PlayerNotFoundException {
        if(player.getId() == 0) throw new PlayerNotFoundException(0);
        findPlayerById(player.getId());
        return playerDAO.save(player);
    }

    @Override
    @Transactional
    public void deletePlayer(int id) throws PlayerNotFoundException{
        playerDAO.delete(id);
    }

}
