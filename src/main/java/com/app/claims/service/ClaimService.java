package com.app.claims.service;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;

import java.time.LocalDate;
import java.util.List;

public interface ClaimService {
    Claim create(ClaimRequest request);
    Claim update(Long id, ClaimRequest request);
    Claim getById(Long id);
    List<Claim> getAll();
    List<Claim> filterByStatus(String status);
    List<Claim> filterByDateRange(LocalDate from, LocalDate to);
    void delete(Long id);
}