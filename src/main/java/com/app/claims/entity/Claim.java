package com.app.claims.entity;

import com.app.claims.entity.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    private Double estimatedAmount;

    private boolean repairRequired;

    private LocalDate claimDate;
}
