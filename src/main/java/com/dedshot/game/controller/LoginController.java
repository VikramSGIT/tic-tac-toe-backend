package com.dedshot.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dedshot.game.service.LoginService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@RequestMapping("/")
@Slf4j
public class LoginController {

    private LoginService loginService;

    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String body, HttpSession session) {
        log.debug("Recived login request: {}.", body);
        return loginService.addPlayer(body, session);
    }

    @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
    @PostMapping("/relogin")
    public ResponseEntity<String> relogin(HttpSession session) {
        log.debug("Recived relogin request.");
        return loginService.loadPlayer(session);
    }
    
}
