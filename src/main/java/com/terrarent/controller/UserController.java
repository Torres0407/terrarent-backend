package com.terrarent.controller;

import com.terrarent.dto.user.UserResponse;
import com.terrarent.entity.User;
import com.terrarent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API for user-related operations")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by ID",
            description = "Retrieves user details by their unique identifier. Accessible by ADMIN or the user themselves.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @userSecurity.isSelf(#id)")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "Unique ID of the user", required = true, in = ParameterIn.PATH)
            @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Get user by email",
            description = "Retrieves user details by their email address. Accessible by ADMIN or the user themselves.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @userSecurity.isSelfByEmail(#email)")
    public ResponseEntity<UserResponse> getUserByEmail(
            @Parameter(description = "Email address of the user", required = true, in = ParameterIn.PATH)
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // Helper for @PreAuthorize (can be in a separate SecurityUtils or component)
    // This class would be a @Component and injected into @PreAuthorize expressions
    @Component("userSecurity")
    public class UserSecurity {
        private final UserService userService;

        public UserSecurity(UserService userService) {
            this.userService = userService;
        }

        public boolean isSelf(UUID userId) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            String currentUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();

            // Use service method that returns User entity
            User user = userService.getUserEntityById(userId); // <-- NEW method
            return user.getEmail().equals(currentUserEmail);
        }

        public boolean isSelfByEmail(String email) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }
            String currentUserEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
            return currentUserEmail.equals(email);
        }
    }

}
