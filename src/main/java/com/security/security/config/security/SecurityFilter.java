package com.security.security.config.security;

import com.security.security.config.security.model.PayloadToken;
import com.security.security.core.model.User;
import com.security.security.service.AuthService;
import com.security.security.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private final AuthService authService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = getToken(request);
        if (isNotBlank(bearerToken)) {
            PayloadToken payloadToken = authService.validateToken(bearerToken);
            if (nonNull(payloadToken)) {
                User user = userService.findByEmail(payloadToken.getEmail());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    private static String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (isNotBlank(authorization)) {
            authorization = authorization.replace("Bearer ", "");
            return authorization;
        }

        return null;
    }
}
