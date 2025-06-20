package com.app.claims.service;

import com.app.claims.entity.User;
import com.app.claims.exception.ResourceNotFound;
import com.app.claims.repository.UserRepository;
import com.app.claims.service.impl.OtpServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OtpServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OtpServiceImpl otpService;

    private User mockUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockUser = User.builder()
                .id(1L)
                .email("bhola@mail.com")
                .password("oldPassword")
                .build();
    }

    @Test
    public void testGenerateOtp_Success() {
        when(userRepository.findByEmail("bhola@mail.com")).thenReturn(Optional.of(mockUser));

        String otp = otpService.generateOtp("bhola@mail.com");

        assertNotNull(otp);
        assertEquals(4, otp.length());
    }

    @Test(expected = ResourceNotFound.class)
    public void testGenerateOtp_UserNotFound() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        otpService.generateOtp("notfound@mail.com");
    }

    @Test
    public void testValidateOtp_Success() {
        when(userRepository.findByEmail("bhola@mail.com")).thenReturn(Optional.of(mockUser));
        String otp = otpService.generateOtp("bhola@mail.com");

        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        boolean isValid = otpService.validateOtp("bhola@mail.com", otp, "newPass123");

        assertTrue(isValid);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testValidateOtp_IncorrectOtp() {
        when(userRepository.findByEmail("bhola@mail.com")).thenReturn(Optional.of(mockUser));
        otpService.generateOtp("bhola@mail.com");

        boolean isValid = otpService.validateOtp("bhola@mail.com", "wrongOtp", "newPass123");

        assertFalse(isValid);
        verify(userRepository, never()).save(any());
    }

    @Test(expected = ResourceNotFound.class)
    public void testValidateOtp_UserNotFound() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        otpService.validateOtp("notfound@mail.com", "1234", "newPass123");
    }
}
