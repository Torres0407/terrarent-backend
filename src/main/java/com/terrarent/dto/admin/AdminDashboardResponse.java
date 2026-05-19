package com.terrarent.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Admin Dashboard metrics")
public class AdminDashboardResponse {
    @Schema(description = "Total number of registered users", example = "150")
    private long totalUsers;
    @Schema(description = "Total number of properties listed", example = "75")
    private long totalProperties;
    @Schema(description = "Number of pending identity verifications", example = "5")
    private long pendingVerifications;
    @Schema(description = "Number of open reports", example = "3")
    private long openReports;
}
