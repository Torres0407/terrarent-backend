package com.terrarent.dto.booking;

import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.user.UserResponse;
import com.terrarent.entity.Booking;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for a booking")
public class BookingResponse {
    @Schema(description = "Unique identifier of the booking", example = "f1a2b3c4-d5e6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "Details of the booked property")
    private PropertyResponse property;
    @Schema(description = "Details of the renter who made the booking")
    private UserResponse renter;
    @Schema(description = "Date of the booking", example = "2026-01-20")
    private LocalDate bookingDate;
    @Schema(description = "Status of the booking", example = "PENDING", allowableValues = {"PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"})
    private Booking.BookingStatus status;
    @Schema(description = "Timestamp when the booking was created", example = "2026-01-17T10:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Timestamp when the booking was last updated", example = "2026-01-17T10:00:00")
    private LocalDateTime updatedAt;
}
