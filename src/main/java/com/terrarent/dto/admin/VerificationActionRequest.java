package com.terrarent.dto.admin;

import com.terrarent.entity.Verification;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for approving or rejecting a verification by Admin")
public class VerificationActionRequest {
    @NotNull(message = "Verification action is required")
    @Schema(description = "Action to perform on the verification", example = "APPROVE", allowableValues = {"APPROVED", "REJECTED"})
    private Verification.VerificationStatus action; // APPROVE or REJECT
}
