package com.terrarent.dto.renter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for scheduling a property tour by a renter")
public class TourRequest {
    @NotNull(message = "Property ID is required")
    @Schema(description = "Unique identifier of the property for the tour", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID propertyId;

    @NotNull(message = "Tour date and time is required")
    @FutureOrPresent(message = "Tour date and time must be in the present or future")
    @Schema(description = "Desired date and time for the tour", example = "2026-01-20T14:30:00")
    private LocalDateTime tourDate;

    @Schema(description = "Optional status for the tour (defaults to PENDING on backend)", example = "PENDING", allowableValues = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"})
    private String status; // Will default to PENDING on backend
}
