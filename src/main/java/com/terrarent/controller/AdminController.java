package com.terrarent.controller;

import com.terrarent.dto.admin.AdminDashboardResponse;
import com.terrarent.dto.admin.FeaturedPropertyUpdateRequest;
import com.terrarent.dto.admin.PropertyStatusUpdateRequest;
import com.terrarent.dto.admin.UserStatusUpdateRequest;
import com.terrarent.dto.admin.VerificationActionRequest;
import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.user.UserResponse;
import com.terrarent.entity.Report;
import com.terrarent.entity.Verification;
import com.terrarent.service.AdminService;
import com.terrarent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "API for administrative operations")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // All admin endpoints require ADMIN role
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

//    private final PropertyService propertyService; // Used for fetching user info implicitly

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userService.getUserIdByEmail(userDetails.getUsername());
        }

        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Get Admin Dashboard Metrics",
            description = "Retrieves key metrics for the admin dashboard, such as total users, properties, pending verifications, and open reports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard metrics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminDashboardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Requires ADMIN role")
    })
    @GetMapping("/dashboard/metrics")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboardMetrics() {
        return ResponseEntity.ok(adminService.getAdminDashboardMetrics());
    }

    @Operation(summary = "Get all users",
            description = "Retrieves a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
    })
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @Operation(summary = "Update user status",
            description = "Updates the status of a specific user (e.g., ACTIVE, SUSPENDED, VERIFIED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping(value = "/users/{userId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> updateUserStatus(
            @Parameter(description = "Unique ID of the user to update", required = true)
            @PathVariable UUID userId,
            @Valid @RequestBody UserStatusUpdateRequest request) {
        return ResponseEntity.ok(adminService.updateUserStatus(userId, request));
    }

    @Operation(summary = "Get all properties",
            description = "Retrieves a list of all properties, regardless of status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved properties",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))))
    })
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {
        return ResponseEntity.ok(adminService.getAllProperties());
    }

    @Operation(summary = "Update property status",
            description = "Updates the status of a specific property (e.g., PENDING, LIVE, REJECTED, DELETED).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property status updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PutMapping(value = "/properties/{propertyId}/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PropertyResponse> updatePropertyStatus(
            @Parameter(description = "Unique ID of the property to update", required = true)
            @PathVariable UUID propertyId,
            @Valid @RequestBody PropertyStatusUpdateRequest request) {
        return ResponseEntity.ok(adminService.updatePropertyStatus(propertyId, request));
    }

    @Operation(summary = "Get pending verifications",
            description = "Retrieves a list of user identity verifications that are pending review.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pending verifications",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Verification.class))))
    })
    @GetMapping("/verifications")
    public ResponseEntity<List<Verification>> getPendingVerifications() {
        return ResponseEntity.ok(adminService.getPendingVerifications());
    }

    @Operation(summary = "Process a user verification",
            description = "Approves or rejects a pending user identity verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verification processed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Verification.class))),
            @ApiResponse(responseCode = "400", description = "Invalid action or verification already processed"),
            @ApiResponse(responseCode = "404", description = "Verification or Admin not found")
    })
    @PostMapping(value = "/verifications/{verificationId}/action", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Verification> processVerification(
            @Parameter(description = "Unique ID of the verification to process", required = true)
            @PathVariable UUID verificationId,
            @Valid @RequestBody VerificationActionRequest request) {
        UUID adminId = getCurrentUserId();
        return ResponseEntity.ok(adminService.processVerification(verificationId, request, adminId));
    }

    @Operation(summary = "Get open reports",
            description = "Retrieves a list of open reports (e.g., property abuse, user misconduct).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved open reports",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Report.class))))
    })
    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getOpenReports() {
        return ResponseEntity.ok(adminService.getOpenReports());
    }

    @Operation(summary = "Resolve a report",
            description = "Marks a specific report as resolved by an administrator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report resolved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Report.class))),
            @ApiResponse(responseCode = "404", description = "Report or Admin not found")
    })
    @PostMapping("/reports/{reportId}/resolve")
    public ResponseEntity<Report> resolveReport(
            @Parameter(description = "Unique ID of the report to resolve", required = true)
            @PathVariable UUID reportId) {
        UUID adminId = getCurrentUserId();
        return ResponseEntity.ok(adminService.resolveReport(reportId, adminId));
    }

    @Operation(summary = "Get featured properties",
            description = "Retrieves the list of properties currently featured on the homepage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved featured properties",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))))
    })
    @GetMapping("/featured-properties")
    public ResponseEntity<List<PropertyResponse>> getFeaturedProperties() {
        return ResponseEntity.ok(adminService.getFeaturedProperties());
    }

    @Operation(summary = "Update featured properties",
            description = "Updates the list and order of properties to be featured on the homepage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Featured properties updated successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))))
    })
    @PutMapping("/featured-properties")
    public ResponseEntity<List<PropertyResponse>> updateFeaturedProperties(@Valid @RequestBody FeaturedPropertyUpdateRequest request) {
        return ResponseEntity.ok(adminService.updateFeaturedProperties(request));
    }
}
