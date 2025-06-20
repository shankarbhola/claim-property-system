package com.app.claims.controller;

import com.app.claims.dto.ForgotPasswordDTO;
import com.app.claims.dto.LoginDTO;
import com.app.claims.dto.RegisterDTO;
import com.app.claims.dto.ResetPasswordDTO;
import com.app.claims.entity.User;
import com.app.claims.service.AuthService;
import com.app.claims.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordDTO request) {
        otpService.generateOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent to registered email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDTO request) {
        if (!otpService.validateOtp(request.getEmail(), request.getOtp(), request.getNewPassword())) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
        return ResponseEntity.ok("Password reset successful");
    }


}