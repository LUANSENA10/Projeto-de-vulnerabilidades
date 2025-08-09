package com.security.security.input.controller;

import com.security.security.core.model.User;
import com.security.security.entity.LoginRequest;
import com.security.security.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// A07 - CÓDIGO SEGURO
/* A07 — Identification and Authentication Failures
        Falhas nos processos de identificação e autenticação de usuários:

        Ataques de força bruta não prevenidos
        Senhas fracas ou padrão permitidas
        Recuperação de senha insegura
        IDs de sessão expostos na URL ou transmitidos de forma insegura
        Tokens de sessão não invalidados após logout
        Autenticação multifator inadequada ou ausente

        Exemplo: Sistema que permite tentativas ilimitadas de login ou tokens de sessão que não expiram. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/login-seguro")
public class A07SecureLoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Controle de tentativas em memória
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Set<String> blacklistedTokens = new HashSet<>();

    // CORREÇÃO: Rate limiting e validações
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request,
                                   HttpServletRequest httpRequest) {

        String clientIp = httpRequest.getRemoteAddr();

        // CORREÇÃO: Rate limiting por IP
        if (loginAttempts.getOrDefault(clientIp, 0) >= 5) {
            return ResponseEntity.status(429).body("Muitas tentativas. Aguarde.");
        }

        User user = userService.findByUsername(request.username());

        // CORREÇÃO: Verificação com hash de senha
        if (user != null && passwordEncoder.matches(request.password(), user.getPassword())) {

            // CORREÇÃO: Reset tentativas após sucesso
            loginAttempts.remove(clientIp);

            // CORREÇÃO: Token com expiração de 15 minutos
            String token = Jwts.builder()
                    .setSubject(request.username())
                    .setExpiration(new Date(System.currentTimeMillis() + 900000)) // 15 min
                    .signWith(SignatureAlgorithm.HS256, getSecretKey()) // Secret seguro
                    .compact();

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "expiresIn", 900
            ));
        }

        // CORREÇÃO: Incrementa contador de tentativas
        loginAttempts.merge(clientIp, 1, Integer::sum);

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    // CORREÇÃO: Logout que invalida token
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = extractToken(request);

        if (token != null) {
            // CORREÇÃO: Adiciona token à blacklist
            blacklistedTokens.add(token);
        }

        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    // CORREÇÃO: Validação de token que verifica blacklist
    public boolean isTokenValid(String token) {
        return !blacklistedTokens.contains(token) &&
                !Jwts.parser().setSigningKey(getSecretKey())
                        .parseClaimsJws(token).getBody()
                        .getExpiration().before(new Date());
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7) : null;
    }

    private String getSecretKey() {
        return "minha-chave-secreta-muito-forte-e-segura-256-bits-minimum";
    }
}