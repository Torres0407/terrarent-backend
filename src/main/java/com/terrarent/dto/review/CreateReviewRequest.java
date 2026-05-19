package com.terrarent.dto.review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Schema(description = "Request DTO for creating a new property review")
public class CreateReviewRequest {
    @NotNull(message = "Property ID is required")
    @Schema(description = "Unique identifier of the property being reviewed", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID propertyId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    @Schema(description = "Rating for the property (1-5 stars)", example = "5")
    private Integer rating;

    @Schema(description = "Optional comment for the review", example = "Great property, very clean and well-maintained.")
    private String comment;
}
