package com.app.claims.dto;

import com.app.claims.entity.enums.ClaimStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClaimRequest {
    @NotBlank
    private String customerName;

    @NotNull
    private ClaimStatus status;

    @NotNull
    private Double estimatedAmount;

    private boolean repairRequired;

    @NotNull
    private LocalDate claimDate;
}
