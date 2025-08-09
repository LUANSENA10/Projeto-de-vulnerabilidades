package com.security.security.controller;

import com.security.security.entity.LoginRequest;
import com.security.security.entity.User;
import com.security.security.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// A07 - CÓDIGO VULNERÁVEL
@RestController
public class A07VulnerableLoginController {
    
    @Autowired
    private UserRepository userRepository;
    
    // VULNERABILIDADE: Sem rate limiting, token sem expiração
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        
        // VULNERABILIDADE: Sem proteção contra força bruta
        User user = userRepository.findByUsername(request.username());
        
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