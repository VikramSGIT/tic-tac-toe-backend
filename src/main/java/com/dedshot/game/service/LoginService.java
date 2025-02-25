package com.dedshot.game.service;

import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;

public interface LoginService {
    ResponseEntity<String> addPlayer(String name, HttpSession session);
}
