package com.terrarent.controller;

import com.terrarent.dto.auth.AuthResponse;
import com.terrarent.dto.auth.LoginRequest;
import com.terrarent.dto.auth.RegisterRequest;
import com.terrarent.dto.auth.ResendVerificationRequest;
import com.terrarent.dto.auth.VerifyEmailRequest;
import com.terrarent.dto.user.UserResponse;
import com.terrarent.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user",
            description = "Registers a new user with the specified details and sends a verification email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or email already registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"timestamp\":\"...\", \"status\":400, \"error\":\"Bad Request\", \"message\":\"Validation failed\", \"errors\":{\"email\":\"Email already registered\"}}")))
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse registeredUser = authService.register(request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate user and get JWT tokens",
            description = "Authenticates a user with email and password, returning access and refresh tokens.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or unverified account",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"timestamp\":\"...\", \"status\":401, \"error\":\"Unauthorized\", \"message\":\"Invalid email or password\"}")))
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Verify user email",
            description = "Verifies a user's email address using a provided code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid verification code"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);
        return ResponseEntity.ok("Email verified successfully");
    }

    @Operation(summary = "Resend email verification code",
            description = "Resends a new email verification code to the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification email resent successfully"),
            @ApiResponse(responseCode = "400", description = "Email already verified"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerificationEmail(@Valid @RequestBody ResendVerificationRequest request) {
        authService.resendVerificationEmail(request);
        return ResponseEntity.ok("Verification email resent successfully");
    }

    @Operation(summary = "Refresh access token",
            description = "Exchanges a valid refresh token for a new access token and refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshTokenHeader
    ) {
        String refreshToken = refreshTokenHeader.substring(7); // Remove "Bearer " prefix
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }
}
