package com.security.security.controller;

import com.security.security.controller.request.UserRequest;
import com.security.security.core.model.User;
import com.security.security.core.model.UserDTO;
import com.security.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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

    @GetMapping
    public ResponseEntity<UserDTO> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody UserRequest req) {
        User user = userService.register(req);

        return ResponseEntity
                .created(URI.create("/v1/users/" + user.getId()))
                .build();
    }

}
