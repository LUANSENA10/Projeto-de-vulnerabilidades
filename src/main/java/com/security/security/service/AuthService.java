package com.security.security.service;

import com.security.security.config.security.Auth0Jwt;
import com.security.security.config.security.model.PayloadToken;
import com.security.security.core.exceptions.UnauthorizedException;
import com.security.security.core.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Auth0Jwt auth0Jwt;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public PayloadToken validateToken(String bearerToken) {
        return auth0Jwt.validateToken(bearerToken);
    }

    public User login(String email, String password) {
        User user = userService.findByEmail(email);
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) throw new UnauthorizedException("Username or password is invalid.");
        return user;
    }
}
