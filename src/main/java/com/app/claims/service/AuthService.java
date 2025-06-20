package com.app.claims.service;

import com.app.claims.dto.AuthResponse;
import com.app.claims.dto.LoginDTO;
import com.app.claims.dto.RegisterDTO;
import org.springframework.stereotype.Service;


public interface AuthService {
    AuthResponse register(RegisterDTO registerDTO);
    AuthResponse login(LoginDTO loginDTO);
}
