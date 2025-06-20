package com.app.claims.service.impl;

import com.app.claims.config.JWTService;
import com.app.claims.dto.AuthResponse;
import com.app.claims.dto.LoginDTO;
import com.app.claims.dto.RegisterDTO;
import com.app.claims.entity.User;
import com.app.claims.exception.ResourceNotFound;
import com.app.claims.repository.UserRepository;
import com.app.claims.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private JWTService jwtService;

    public AuthServiceImpl(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterDTO registerDTO) {

        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(new BCryptPasswordEncoder().encode(registerDTO.getPassword()))
                .role(registerDTO.getRole())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);

    }
}
