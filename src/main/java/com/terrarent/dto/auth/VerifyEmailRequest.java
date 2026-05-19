package com.terrarent.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for email verification")
public class VerifyEmailRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Verification code is required")
    @Schema(description = "6-digit verification code sent to email", example = "123456")
    private String code;
}
