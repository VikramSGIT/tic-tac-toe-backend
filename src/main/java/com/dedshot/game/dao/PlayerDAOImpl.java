package com.dedshot.game.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.dedshot.game.entity.Player;
import com.dedshot.game.errors.PlayerNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class PlayerDAOImpl implements PlayerDAO {
    private EntityManager entityManager;

    @Override
    public List<Player> findAll() {
        TypedQuery<Player> typedQuery = entityManager.createQuery("FROM Player", Player.class);
        return typedQuery.getResultList();
    }

    @Override
    public Player findById(int id) throws PlayerNotFoundException {
        Player player = entityManager.find(Player.class, id);
        if(player == null) throw new PlayerNotFoundException(id);
        
        return player;
    }

    @Override
    public Player save(Player player) {
        return entityManager.merge(player);
    }

    @Override
    public void delete(int id) throws PlayerNotFoundException {
        entityManager.remove(findById(id));
    }
}
