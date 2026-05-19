package com.terrarent.dto.auth;

import com.terrarent.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for new user registration")
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Schema(description = "User's password (must meet complexity requirements)", example = "SecureP@ss1")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Phone number should be valid")
    @Schema(description = "User's phone number", example = "+15551234567")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    @Schema(description = "User's role", example = "ROLE_RENTER", allowableValues = {"ROLE_RENTER", "ROLE_LANDLORD", "ROLE_ADMIN"})
    private Role.RoleName role;
}
