package com.app.claims.repository;

import com.app.claims.entity.Claim;
import com.app.claims.entity.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByStatus(ClaimStatus status);
    List<Claim> findByClaimDateBetween(LocalDate startDate, LocalDate endDate);
}