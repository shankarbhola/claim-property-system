package com.app.claims.service;

import com.app.claims.config.JWTService;
import com.app.claims.dto.AuthResponse;
import com.app.claims.dto.LoginDTO;
import com.app.claims.dto.RegisterDTO;
import com.app.claims.entity.User;
import com.app.claims.entity.enums.Role;
import com.app.claims.repository.UserRepository;
import com.app.claims.service.impl.AuthServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private BCryptPasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    public void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("bhola");
        registerDTO.setEmail("bhola@mail.com");
        registerDTO.setPassword("bhola123");
        registerDTO.setRole(Role.CUSTOMER);

        when(userRepository.findByEmail(registerDTO.getEmail())).thenReturn(Optional.empty());
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerDTO);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRegister_EmailAlreadyExists() {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("exist@mail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));
        authService.register(dto);
    }

    @Test
    public void testLogin_Success() {
        String rawPassword = "bhola123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("user@mail.com");
        loginDTO.setPassword(rawPassword);

        User user = User.builder()
                .email(loginDTO.getEmail())
                .password(encodedPassword)
                .build();

        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(loginDTO);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test(expected = RuntimeException.class)
    public void testLogin_UserNotFound() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("invalid@mail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        authService.login(dto);
    }

    @Test(expected = RuntimeException.class)
    public void testLogin_InvalidPassword() {
        String correctPassword = "correct";
        String wrongPassword = "wrong";

        User user = User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode(correctPassword))
                .build();

        LoginDTO dto = new LoginDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword(wrongPassword);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        authService.login(dto);
    }
}
