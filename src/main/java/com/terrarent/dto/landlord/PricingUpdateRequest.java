package com.terrarent.dto.landlord;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating property pricing by Landlord")
public class PricingUpdateRequest {
    @DecimalMin(value = "0.01", message = "Annual price must be positive")
    @Schema(description = "New annual price for the property", example = "18000.00")
    private BigDecimal annualPrice;
    
    @DecimalMin(value = "0.01", message = "Nightly price must be positive")
    @Schema(description = "New nightly price for the property", example = "150.00")
    private BigDecimal nightlyPrice;
}
