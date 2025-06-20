package com.app.claims.controller;

import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;
import com.app.claims.entity.enums.ClaimStatus;
import com.app.claims.service.ClaimService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ClaimControllerTest {

    @Mock
    private ClaimService claimService;

    @InjectMocks
    private ClaimController claimController;

    private Claim sampleClaim;

    @Before
    public void setUp() {
        sampleClaim = Claim.builder()
                .id(1L)
                .customerName("Bhola Shankar")
                .status(ClaimStatus.PENDING)
                .estimatedAmount(5000.0)
                .repairRequired(true)
                .claimDate(LocalDate.of(2025, 6, 19))
                .build();
    }

    @Test
    public void testCreateClaim() {
        ClaimRequest request = new ClaimRequest("Bhola Shankar", ClaimStatus.PENDING, 5000.0, true, LocalDate.of(2025, 6, 19));
        when(claimService.create(request)).thenReturn(sampleClaim);

        ResponseEntity<Claim> response = claimController.createClaim(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleClaim, response.getBody());
    }

    @Test
    public void testUpdateClaim() {
        ClaimRequest request = new ClaimRequest("Bhola Shankar", ClaimStatus.APPROVED, 6000.0, false, LocalDate.now());
        when(claimService.update(1L, request)).thenReturn(sampleClaim);

        ResponseEntity<Claim> response = claimController.updateClaim(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleClaim, response.getBody());
    }

    @Test
    public void testGetClaim() {
        when(claimService.getById(1L)).thenReturn(sampleClaim);

        ResponseEntity<Claim> response = claimController.getClaim(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleClaim, response.getBody());
    }

    @Test
    public void testListClaims_All() {
        List<Claim> claims = Collections.singletonList(sampleClaim);
        when(claimService.getAll()).thenReturn(claims);

        ResponseEntity<List<Claim>> response = claimController.listClaims(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(claims, response.getBody());
    }

    @Test
    public void testListClaims_ByStatus() {
        List<Claim> claims = Arrays.asList(sampleClaim);
        when(claimService.filterByStatus("PENDING")).thenReturn(claims);

        ResponseEntity<List<Claim>> response = claimController.listClaims("PENDING", null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(claims, response.getBody());
    }

    @Test
    public void testListClaims_ByDateRange() {
        List<Claim> claims = Arrays.asList(sampleClaim);
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 30);
        when(claimService.filterByDateRange(from, to)).thenReturn(claims);

        ResponseEntity<List<Claim>> response = claimController.listClaims(null, from, to);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(claims, response.getBody());
    }

    @Test
    public void testDeleteClaim() {
        doNothing().when(claimService).delete(1L);

        ResponseEntity<String> response = claimController.deleteClaim(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Claim deleted successfully", response.getBody());
        verify(claimService).delete(1L);
    }
    
    @Test
    public void testListClaims_OnlyFromDateProvided() {
        List<Claim> claims = Arrays.asList(sampleClaim);
        when(claimService.getAll()).thenReturn(claims);

        LocalDate from = LocalDate.of(2025, 6, 1);
        
        ResponseEntity<List<Claim>> response = claimController.listClaims(null, from, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(claims, response.getBody());
    }

}
