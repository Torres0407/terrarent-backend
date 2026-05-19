package com.terrarent.dto.renter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Renter Dashboard metrics")
public class RenterDashboardResponse {

    // ✅ User info
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;

    // ✅ Metrics
    private long savedPropertiesCount;
    private long bookingsCount;
    private long applicationsCount;
    private long toursCount;
}

