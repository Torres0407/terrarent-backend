package com.terrarent.dto.property;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for filtering properties")
public class PropertyFilterRequest {
    @Schema(description = "Address or part of an address to search for", example = "Main St")
    private String address;

    @DecimalMin(value = "0.0", message = "Minimum price cannot be negative")
    @Schema(description = "Minimum price for property search", example = "1000.00")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", message = "Maximum price cannot be negative")
    @Schema(description = "Maximum price for property search", example = "2000.00")
    private BigDecimal maxPrice;

    @Min(value = 0, message = "Minimum bedrooms cannot be negative")
    @Schema(description = "Minimum number of bedrooms", example = "1")
    private Integer minBedrooms;

    @Min(value = 0, message = "Maximum bedrooms cannot be negative")
    @Schema(description = "Maximum number of bedrooms", example = "3")
    private Integer maxBedrooms;

    @Min(value = 0, message = "Minimum bathrooms cannot be negative")
    @Schema(description = "Minimum number of bathrooms", example = "1")
    private Integer minBathrooms;

    @Min(value = 0, message = "Maximum bathrooms cannot be negative")
    @Schema(description = "Maximum number of bathrooms", example = "2")
    private Integer maxBathrooms;
}
