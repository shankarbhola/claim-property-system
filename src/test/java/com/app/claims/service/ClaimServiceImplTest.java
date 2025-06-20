package com.app.claims.service;


import com.app.claims.dto.ClaimRequest;
import com.app.claims.entity.Claim;
import com.app.claims.entity.enums.ClaimStatus;
import com.app.claims.exception.ResourceNotFound;
import com.app.claims.repository.ClaimRepository;
import com.app.claims.service.NotificationService;
import com.app.claims.service.impl.ClaimServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClaimServiceImplTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClaimServiceImpl claimService;

    private Claim sampleClaim;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

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
        ClaimRequest request = new ClaimRequest("Bhola Shankar", ClaimStatus.PENDING, 5000.0, true, LocalDate.now());

        when(claimRepository.save(any(Claim.class))).thenReturn(sampleClaim);

        Claim saved = claimService.create(request);

        assertNotNull(saved);
        verify(claimRepository).save(any(Claim.class));
    }

    @Test
    public void testUpdateClaim_WithStatusChangeToApproved() {
        ClaimRequest request = new ClaimRequest("Bhola Shankar", ClaimStatus.APPROVED, 5000.0, true, LocalDate.now());

        when(claimRepository.findById(1L)).thenReturn(Optional.of(sampleClaim));
        when(claimRepository.save(any(Claim.class))).thenReturn(sampleClaim);

        Claim updated = claimService.update(1L, request);

        assertNotNull(updated);
        verify(notificationService).sendEmail(request);
        verify(notificationService).sendSMS(request);
        verify(notificationService).generatePDF(1L, request);
        verify(claimRepository).save(any(Claim.class));
    }

    @Test
    public void testUpdateClaim_WithoutStatusChange() {
        ClaimRequest request = new ClaimRequest("Bhola Shankar", ClaimStatus.PENDING, 5000.0, true, LocalDate.now());

        when(claimRepository.findById(1L)).thenReturn(Optional.of(sampleClaim));
        when(claimRepository.save(any(Claim.class))).thenReturn(sampleClaim);

        Claim updated = claimService.update(1L, request);

        assertNotNull(updated);
        verify(notificationService, never()).sendEmail(any());
        verify(notificationService, never()).sendSMS(any());
        verify(notificationService, never()).generatePDF(anyLong(), any());
    }

    @Test
    public void testGetById_Success() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(sampleClaim));

        Claim claim = claimService.getById(1L);

        assertEquals("Bhola Shankar", claim.getCustomerName());
    }

    @Test(expected = ResourceNotFound.class)
    public void testGetById_NotFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        claimService.getById(1L);
    }

    @Test
    public void testGetAllClaims() {
        when(claimRepository.findAll()).thenReturn(Arrays.asList(sampleClaim));

        List<Claim> list = claimService.getAll();

        assertEquals(1, list.size());
    }

    @Test
    public void testFilterByStatus() {
        when(claimRepository.findByStatus(ClaimStatus.PENDING)).thenReturn(Arrays.asList(sampleClaim));

        List<Claim> list = claimService.filterByStatus("pending");

        assertEquals(1, list.size());
    }

    @Test
    public void testFilterByDateRange() {
        LocalDate from = LocalDate.of(2025, 6, 1);
        LocalDate to = LocalDate.of(2025, 6, 30);

        when(claimRepository.findByClaimDateBetween(from, to)).thenReturn(Arrays.asList(sampleClaim));

        List<Claim> result = claimService.filterByDateRange(from, to);

        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteClaim() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(sampleClaim));
        doNothing().when(claimRepository).delete(sampleClaim);

        claimService.delete(1L);

        verify(claimRepository).delete(sampleClaim);
    }
}
