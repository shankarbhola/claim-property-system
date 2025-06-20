package com.app.claims.controller;

import com.app.claims.dto.*;
import com.app.claims.entity.enums.Role;
import com.app.claims.service.AuthService;
import com.app.claims.service.OtpService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("test@example.com");
        registerDTO.setPassword("pass");
        registerDTO.setRole(Role.CUSTOMER);
        AuthResponse authtoken = new AuthResponse("testtoken");

        when(authService.register(registerDTO)).thenReturn(authtoken);

        ResponseEntity<?> response = authController.register(registerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authtoken, response.getBody());
        verify(authService).register(registerDTO);
    }

    @Test
    public void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("user@example.com");
        loginDTO.setPassword("pass123");

        AuthResponse authtoken = new AuthResponse("testtoken");
        when(authService.login(loginDTO)).thenReturn(authtoken);
        ResponseEntity<?> response = authController.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authtoken, response.getBody());
    }

    @Test
    public void testForgotPassword_Success() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO();
        dto.setEmail("forgot@example.com");

        ResponseEntity<String> response = authController.forgotPassword(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP sent to registered email.", response.getBody());
        verify(otpService).generateOtp(dto.getEmail());
    }

    @Test
    public void testResetPassword_Success() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setEmail("reset@example.com");
        dto.setOtp("123456");
        dto.setNewPassword("newPass");

        when(otpService.validateOtp(dto.getEmail(), dto.getOtp(), dto.getNewPassword())).thenReturn(true);

        ResponseEntity<String> response = authController.resetPassword(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful", response.getBody());
    }

    @Test
    public void testResetPassword_Failure_InvalidOtp() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setEmail("reset@example.com");
        dto.setOtp("wrong");
        dto.setNewPassword("newPass");

        when(otpService.validateOtp(dto.getEmail(), dto.getOtp(), dto.getNewPassword())).thenReturn(false);

        ResponseEntity<String> response = authController.resetPassword(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid or expired OTP", response.getBody());
    }
}
