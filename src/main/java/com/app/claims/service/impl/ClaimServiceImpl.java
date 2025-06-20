package com.app.claims.service.impl;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;
import com.app.claims.entity.enums.ClaimStatus;
import com.app.claims.exception.ResourceNotFound;
import com.app.claims.repository.ClaimRepository;
import com.app.claims.service.ClaimService;
import com.app.claims.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {

    private ClaimRepository claimRepository;
    private NotificationService notificationService;

    public ClaimServiceImpl(ClaimRepository claimRepository, NotificationService notificationService) {
        this.claimRepository = claimRepository;
        this.notificationService = notificationService;
    }


    @Override
    public Claim create(ClaimRequest request) {
        Claim claim = Claim.builder()
                .customerName(request.getCustomerName())
                .status(request.getStatus())
                .estimatedAmount(request.getEstimatedAmount())
                .repairRequired(request.isRepairRequired())
                .claimDate(request.getClaimDate())
                .build();
        return claimRepository.save(claim);
    }

    @Override
    public Claim update(Long id, ClaimRequest request) {

        Claim claim = getById(id);
        if (!claim.getStatus().equals(request.getStatus()) && request.getStatus() == ClaimStatus.APPROVED) {
            notificationService.sendEmail(request);
            notificationService.sendSMS(request);
            notificationService.generatePDF(id,request);
        }
        claim.setCustomerName(request.getCustomerName());
        claim.setStatus(request.getStatus());
        claim.setEstimatedAmount(request.getEstimatedAmount());
        claim.setRepairRequired(request.isRepairRequired());
        claim.setClaimDate(request.getClaimDate());
        return claimRepository.save(claim);
    }

    @Override
    public Claim getById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Claim not found with ID: " + id));
    }

    @Override
    public List<Claim> getAll() {
        return claimRepository.findAll();
    }

    @Override
    public List<Claim> filterByStatus(String status) {
        return claimRepository.findByStatus(ClaimStatus.valueOf(status.toUpperCase()));
    }

    @Override
    public List<Claim> filterByDateRange(LocalDate from, LocalDate to) {
        return claimRepository.findByClaimDateBetween(from, to);
    }

    @Override
    public void delete(Long id) {
        Claim claim = getById(id);
        claimRepository.delete(claim);
    }
}
