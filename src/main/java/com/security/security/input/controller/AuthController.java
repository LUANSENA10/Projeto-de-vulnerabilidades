package com.security.security.input.controller;

import com.security.security.config.security.Auth0Jwt;
import com.security.security.config.security.model.BearerToken;
import com.security.security.core.model.User;
import com.security.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final Auth0Jwt auth0Jwt;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<BearerToken> login(String email, String password) {
        User user = authService.login(email, password);
        String token = auth0Jwt.generateToken(user);
        return ResponseEntity.ok(new BearerToken(token));
    }
}
