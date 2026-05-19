package com.terrarent.dto.landlord;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Landlord Dashboard metrics")
public class LandlordDashboardResponse {
    @Schema(description = "Total revenue generated from landlord's properties", example = "15000.50")
    private BigDecimal totalRevenue;
    @Schema(description = "Occupancy rate of landlord's properties (as a percentage)", example = "0.85")
    private BigDecimal occupancyRate; // As a percentage or decimal
    @Schema(description = "Total number of properties owned by the landlord", example = "10")
    private long totalProperties;
    @Schema(description = "Number of active property listings by the landlord", example = "8")
    private long activeListings;
}
