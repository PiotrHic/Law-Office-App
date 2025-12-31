package org.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(@RequestHeader("X-User") String user,
                        @RequestHeader("X-Roles") String roles) {
        return "Cześć " + user + "! Twoje role: " + roles;
    }
}