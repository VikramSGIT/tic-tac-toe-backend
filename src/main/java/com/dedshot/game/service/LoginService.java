package com.dedshot.game.service;

import org.springframework.http.ResponseEntity;
import com.dedshot.game.entity.DataPlayer;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    ResponseEntity<DataPlayer> addPlayer(String name, HttpSession session);
}
