package org.example.apigateway.controller;

import org.example.apigateway.service.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtil jwtService;

    public AuthController( JwtUtil jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username) {
        // na potrzeby testu dajemy rolę USER
        String token = jwtService.generateToken(username, List.of("ROLE_USER"));
        return Map.of("token", token);
    }
}

