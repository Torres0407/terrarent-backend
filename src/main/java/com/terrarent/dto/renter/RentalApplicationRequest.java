package com.terrarent.dto.renter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new rental application by a renter")
public class RentalApplicationRequest {
    @NotNull(message = "Property ID is required")
    @Schema(description = "Unique identifier of the property to apply for", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID propertyId;

    @Schema(description = "Optional status for the application (defaults to PENDING on backend)", example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED", "WITHDRAWN"})
    private String status; // Will default to PENDING on backend
}
