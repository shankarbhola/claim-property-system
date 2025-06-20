package com.app.claims.service.impl;

import com.app.claims.entity.User;
import com.app.claims.exception.ResourceNotFound;
import com.app.claims.repository.UserRepository;
import com.app.claims.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private UserRepository userRepository;

    private final Map<String, String> otpMap = new HashMap<>();

    @Override
    public String generateOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User not found"));

        String otp = "";
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 4; i++) {
            int randomNumber = random.nextInt(10);
            otp = otp + randomNumber;
        }
        if (otp.length() == 4) {
            otpMap.put(email,otp);
            System.out.println("Sending OTP to " + email + ": " + otp);
            return otp;
        }
        return "Errer sending OTP";
    }

    @Override
    public boolean validateOtp(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
        if (otp.equals(otpMap.get(email))){
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            User save = userRepository.save(user);
            return true;
        }
        return false;
    }
}
