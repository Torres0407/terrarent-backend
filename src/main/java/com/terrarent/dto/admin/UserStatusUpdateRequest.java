package com.terrarent.dto.admin;

import com.terrarent.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating user status by Admin")
public class UserStatusUpdateRequest {
    @NotNull(message = "User status is required")
    @Schema(description = "New status for the user", example = "ACTIVE", allowableValues = {"PENDING_VERIFICATION", "ACTIVE", "SUSPENDED", "VERIFIED"})
    private User.UserStatus status;
}
