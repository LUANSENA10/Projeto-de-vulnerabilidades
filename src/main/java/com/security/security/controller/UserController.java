package com.security.security.controller;

import com.security.security.core.model.User;
import com.security.security.core.model.UserDTO;
import com.security.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper mapper;

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }
}
