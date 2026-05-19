package com.terrarent.dto.auth;

import com.terrarent.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for authentication, containing JWT tokens and user details")
public class AuthResponse {
    @Schema(description = "JWT Access Token for authenticated API calls", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwicm9sZXMiOiJST0xFX1JFTlRFUiIsImlhdCI6MTY3ODg4NjQwMCwiZXhwIjoxNjc4ODkwMDAwfQ.signature")
    private String accessToken;

    @Schema(description = "JWT Refresh Token for obtaining new access tokens", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNjc4ODg2NDAwLCJleHAiOjE2Nzk0OTA4MDB9.signature")
    private String refreshToken;

    @Schema(description = "Details of the authenticated user")
    private UserResponse user;
}
