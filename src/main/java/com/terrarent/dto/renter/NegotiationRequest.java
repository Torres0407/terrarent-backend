package com.terrarent.dto.renter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new negotiation offer by a renter")
public class NegotiationRequest {
    @NotNull(message = "Offer amount is required")
    @DecimalMin(value = "0.01", message = "Offer amount must be positive")
    @Schema(description = "The proposed offer amount", example = "1450.00")
    private BigDecimal offer;

    @Schema(description = "Optional status for the negotiation (defaults to PENDING on backend)", example = "PENDING", allowableValues = {"PENDING", "ACCEPTED", "REJECTED", "WITHDRAWN"})
    private String status; // Will default to PENDING on backend
}
