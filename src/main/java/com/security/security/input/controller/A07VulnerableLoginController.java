package com.security.security.input.controller;

import com.security.security.core.model.User;
import com.security.security.entity.LoginRequest;
import com.security.security.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// A07 - CÓDIGO VULNERÁVEL
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/login-vulneravel")
public class A07VulnerableLoginController {

    private final UserService userService;

    // VULNERABILIDADE: Sem rate limiting, token sem expiração
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // VULNERABILIDADE: Sem proteção contra força bruta
        User user = userService.findByUsername(request.username());

        // VULNERABILIDADE: Senha em texto plano
        if (user != null && user.getPassword().equals(request.password())) {

            // VULNERABILIDADE: Token que nunca expira
            String token = Jwts.builder()
                    .setSubject(request.username())
                    .signWith(SignatureAlgorithm.HS256, "secreto123") // Secret fraco
                    // SEM setExpiration()
                    .compact();

            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(401).body("Login inválido");
    }

    // VULNERABILIDADE: Logout não invalida token
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Não faz nada
        return ResponseEntity.ok("Logout realizado");
    }
}