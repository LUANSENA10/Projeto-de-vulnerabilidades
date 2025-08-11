package com.security.security.controller.request;

import com.security.security.core.model.HashMode;

public record UserRequest(
        String username,
        String email,
        String password,
        HashMode mode
) {}