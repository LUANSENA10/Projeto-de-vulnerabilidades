package com.security.security.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.security.security.config.properties.TokenProperties;
import com.security.security.core.exceptions.InvalidTokenException;
import com.security.security.core.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Auth0Jwt {

    private static final int EXPIRATION_HOURS = 1;
    private static final String OFFSET_ID = "-03:00";
    private static final String SECURITY_API = "Security API";

    private final TokenProperties tokenProperties;

    public String generateToken(User user) {
        try {
            return JWT.create()
                    .withIssuer(SECURITY_API)
                    .withSubject(user.getEmail())
                    .withClaim("username", user.getUsername())
                    .withClaim("email", user.getEmail())
                    .withIssuedAt(issuedAt())
                    .withExpiresAt(expiresAt())
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(Algorithm.HMAC256(tokenProperties.getSecret()));
        } catch (JWTCreationException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    public String validateToken(String token) {
        try {
            Map<String, Claim> claims = JWT.require(Algorithm.HMAC256(tokenProperties.getSecret()))
                    .withIssuer(SECURITY_API)
                    .build()
                    .verify(token)
                    .getClaims();
            return claims.get("email").asString();
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    private Instant issuedAt() {
        return LocalDateTime.now().toInstant(ZoneOffset.of(OFFSET_ID));
    }

    private Instant expiresAt() {
        return LocalDateTime.now().plusHours(EXPIRATION_HOURS).toInstant(ZoneOffset.of(OFFSET_ID));
    }
}
