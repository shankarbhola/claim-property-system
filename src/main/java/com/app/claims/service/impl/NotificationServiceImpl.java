package com.app.claims.service.impl;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;
import com.app.claims.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${pdf.output-dir:pdfs}")
    public String pdfDir;

    @Override
    public void sendEmail(ClaimRequest claim) {
        log.info("Email sent to {} for claim approval. Amount: {}", claim.getCustomerName(), claim.getEstimatedAmount());
    }

    @Override
    public void sendSMS(ClaimRequest claim) {
        log.info("SMS sent to {}: Your claim has been approved!", claim.getCustomerName());
    }

    @Override
    public void generatePDF(Long id, ClaimRequest claim) {
        try {
            File folder = new File(pdfDir);
            if (!folder.exists()) folder.mkdirs();

            File pdfFile = new File(folder, "claim_" + id + ".pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            document.add(new Paragraph("Claim Summary"));
            document.add(new Paragraph("---------------------------"));
            document.add(new Paragraph("Claim ID: " + id));
            document.add(new Paragraph("Customer Name: " + claim.getCustomerName()));
            document.add(new Paragraph("Status: " + claim.getStatus()));
            document.add(new Paragraph("Estimated Amount: $" + claim.getEstimatedAmount()));
            document.add(new Paragraph("Repair Required: " + (claim.isRepairRequired() ? "Yes" : "No")));
            document.add(new Paragraph("Claim Date: " + claim.getClaimDate()));

            document.close();

            log.info("PDF generated at: {}", pdfFile.getAbsolutePath());

        } catch (IOException | DocumentException e) {
            log.error("Failed to generate PDF: {}", e.getMessage());
        }
    }
    }