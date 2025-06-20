package com.app.claims.controller;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;
import com.app.claims.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<Claim> createClaim(@RequestBody @Valid ClaimRequest request) {
        return ResponseEntity.ok(claimService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @PutMapping("/{id}")
    public ResponseEntity<Claim> updateClaim(@PathVariable Long id, @RequestBody @Valid ClaimRequest request) {
        //return ResponseEntity.ok();
        return new ResponseEntity<>(claimService.update(id, request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @GetMapping
    public ResponseEntity<List<Claim>> listClaims(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        if (status != null) return ResponseEntity.ok(claimService.filterByStatus(status));
        if (fromDate != null && toDate != null) return ResponseEntity.ok(claimService.filterByDateRange(fromDate, toDate));
        return ResponseEntity.ok(claimService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable Long id) {
        claimService.delete(id);
        return ResponseEntity.ok("Claim deleted successfully");
    }
}
