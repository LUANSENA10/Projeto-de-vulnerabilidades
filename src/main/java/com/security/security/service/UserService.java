package com.security.security.service;

import com.security.security.controller.request.UserRequest;
import com.security.security.core.exceptions.UserNotFoundException;
import com.security.security.core.model.User;
import com.security.security.repository.UserRepository;
import com.security.security.repository.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final SecureRandom secureRandom = new SecureRandom();
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    public User findById(UUID id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.map(userEntity, User.class);
    }

    public User findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.map(userEntity, User.class);
    }

    public User findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.map(userEntity, User.class);
    }

    public User register(UserRequest req) {
        return switch (req.mode()) {
            case MD5_NO_SALT -> registerMd5NoSalt(req);
            case MD5_SALT    -> registerMd5WithSalt(req);
            case BCRYPT      -> registerBcrypt(req);
        };
    }

    private User registerMd5NoSalt(UserRequest req) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(req.password().getBytes(StandardCharsets.UTF_8));
            String md5 = HexFormat.of().formatHex(digest);

            UserEntity user = new UserEntity(req.username(), req.email(), md5, null, "MD5");
            user = userRepository.save(user);
            return mapper.map(user, User.class);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 not available", e);
        }
    }

    private User registerMd5WithSalt(UserRequest req) {
        try {
            byte[] saltBytes = new byte[16];
            secureRandom.nextBytes(saltBytes);
            String saltB64 = Base64.getEncoder().encodeToString(saltBytes);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(saltBytes);
            md.update(req.password().getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            String md5 = HexFormat.of().formatHex(digest);

            UserEntity user = new UserEntity(req.username(), req.email(), md5, saltB64, "MD5+SALT");
            user = userRepository.save(user);
            return mapper.map(user, User.class);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 not available", e);
        }
    }

    private User registerBcrypt(UserRequest req) {
        String hash = bcrypt.encode(req.password());
        UserEntity user = new UserEntity(req.username(), req.email(), hash, null, "BCRYPT");
        user = userRepository.save(user);

        return mapper.map(user, User.class);
    }

}
