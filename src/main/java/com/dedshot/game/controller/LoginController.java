package com.dedshot.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dedshot.game.service.LoginService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/")
public class LoginController {

    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> postMethodName(@RequestBody String body, HttpSession session) {
        return loginService.addPlayer(body, session);
    }
    
}
