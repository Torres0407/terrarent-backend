package com.terrarent.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new booking")
public class CreateBookingRequest {
    @NotNull(message = "Property ID is required")
    @Schema(description = "Unique identifier of the property to book", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID propertyId;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be in the present or future")
    @Schema(description = "Desired date for the booking", example = "2026-01-20")
    private LocalDate bookingDate;

    @Schema(description = "Optional status for the booking (defaults to PENDING on backend)", example = "PENDING", allowableValues = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"})
    private String status;
}
