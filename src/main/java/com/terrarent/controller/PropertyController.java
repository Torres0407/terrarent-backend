//package com.terrarent.controller;
//
//import com.terrarent.dto.property.PropertyFilterRequest;
//import com.terrarent.dto.property.PropertyResponse;
//import com.terrarent.dto.util.PageResponse;
//import com.terrarent.entity.Property;
//import com.terrarent.entity.User;
//import com.terrarent.service.LandlordService;
//import com.terrarent.service.PropertyService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/properties")
//@RequiredArgsConstructor
//@PreAuthorize("hasAnyAuthority('ROLE_LANDLORD', 'ROLE_ADMIN')")
//@Tag(name = "Properties", description = "Public API for property search and details")
//public class PropertyController {
//
//    private final PropertyService propertyService;
//    private final LandlordService landlordService;
//    private UUID getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
//            return propertyService.getUserIdByEmail(userDetails.getUsername());
//        }
//        throw new RuntimeException("User not authenticated.");
//    }
//
//    @GetMapping
//    public ResponseEntity<List<PropertyResponse>> getMyProperties() {
//        UUID landlordId = getCurrentUserId();
//        return ResponseEntity.ok(landlordService.getLandlordProperties(landlordId));
//    }
//}
//
//
//    @Operation(summary = "Get all properties with optional filters",
//            description = "Retrieves a paginated list of properties, optionally filtered by address, price range, and number of bedrooms/bathrooms.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved properties",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = PageResponse.class)))
//    })
//    @GetMapping
//    public ResponseEntity<PageResponse<PropertyResponse>> getAllProperties(
//            @Valid PropertyFilterRequest filters,
//            @Parameter(description = "Page number (0-indexed)", example = "0")
//            @RequestParam(defaultValue = "0") int page,
//            @Parameter(description = "Number of items per page", example = "10")
//            @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(propertyService.getAllProperties(filters, page, size));
//    }
//
//    @Operation(summary = "Get property by ID",
//            description = "Retrieves detailed information for a single property by its unique ID.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Property found",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyResponse.class))),
//            @ApiResponse(responseCode = "404", description = "Property not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<PropertyResponse> getPropertyById(
//            @Parameter(description = "Unique ID of the property", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
//            @PathVariable UUID id) {
//        return ResponseEntity.ok(propertyService.getPropertyById(id));
//    }
//
//
//}



package com.terrarent.controller;

import com.terrarent.dto.property.PropertyFilterRequest;
import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.util.PageResponse;
import com.terrarent.service.LandlordService;
import com.terrarent.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Public and landlord property APIs")
public class PropertyController {

    private final PropertyService propertyService;
    private final LandlordService landlordService;

    /* =========================================================
       LANDLORD: Get my properties
       ========================================================= */
    @Operation(summary = "Get properties owned by the current landlord")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved properties",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('ROLE_LANDLORD', 'ROLE_ADMIN')")
    public ResponseEntity<List<PropertyResponse>> getMyProperties(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UUID landlordId = propertyService.getUserIdByEmail(userDetails.getUsername());

        return ResponseEntity.ok(landlordService.getLandlordProperties(landlordId));
    }

    /* =========================================================
       PUBLIC: Get all properties with filters
       ========================================================= */
    @Operation(summary = "Get all properties with optional filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved properties",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PageResponse<PropertyResponse>> getAllProperties(
            @Valid PropertyFilterRequest filters,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page")
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(propertyService.getAllProperties(filters, page, size));
    }

    /* =========================================================
       PUBLIC: Get property by ID
       ========================================================= */
    @Operation(summary = "Get property by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Property found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(
            @Parameter(description = "Unique ID of the property", required = true)
            @PathVariable UUID id) {

        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }
}
