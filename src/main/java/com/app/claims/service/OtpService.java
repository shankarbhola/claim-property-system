package com.app.claims.service;

public interface OtpService {
    String generateOtp(String email);
    boolean validateOtp(String email, String otp, String newPassword);
}
