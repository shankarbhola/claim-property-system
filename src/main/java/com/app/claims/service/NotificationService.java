package com.app.claims.service;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;

public interface NotificationService {
    void sendEmail(ClaimRequest claim);
    void sendSMS(ClaimRequest claim);
    void generatePDF(Long id, ClaimRequest claim);
}
