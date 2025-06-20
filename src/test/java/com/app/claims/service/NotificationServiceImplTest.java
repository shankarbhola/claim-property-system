package com.app.claims.service;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.enums.ClaimStatus;
import com.app.claims.service.NotificationService;
import com.app.claims.service.impl.NotificationServiceImpl;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceImplTest {

    private NotificationServiceImpl notificationService;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() {
        notificationService = new NotificationServiceImpl();
        notificationService.pdfDir = tempFolder.getRoot().getAbsolutePath(); 
    }

    private ClaimRequest sampleClaim() {
        return new ClaimRequest("Bhola Shankar", ClaimStatus.APPROVED, 7500.0, true, LocalDate.of(2025, 6, 20));
    }

    @Test
    public void testSendEmail() {
        notificationService.sendEmail(sampleClaim());
    }

    @Test
    public void testSendSMS() {
        notificationService.sendSMS(sampleClaim());
    }

    @Test
    public void testGeneratePDF() {
        Long claimId = 101L;
        ClaimRequest claim = sampleClaim();

        notificationService.generatePDF(claimId, claim);

        File generated = new File(tempFolder.getRoot(), "claim_" + claimId + ".pdf");
        assertTrue("PDF should be generated", generated.exists());
        assertTrue("PDF should not be empty", generated.length() > 0);
    }
}

