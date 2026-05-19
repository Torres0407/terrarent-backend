package com.terrarent.controller;

import com.terrarent.dto.booking.BookingResponse;
import com.terrarent.dto.landlord.LandlordDashboardResponse;
import com.terrarent.dto.landlord.PricingUpdateRequest;
import com.terrarent.dto.property.PropertyRequest;
import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.service.ImageService;
import com.terrarent.service.LandlordService;
import com.terrarent.service.PropertyService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/landlord")
@RequiredArgsConstructor
@Tag(name = "Landlord", description = "API for landlord-specific operations")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("hasAnyAuthority('ROLE_LANDLORD', 'ROLE_ADMIN')") // All landlord endpoints require these roles
public class LandlordController {

    private final LandlordService landlordService;
    private final PropertyService propertyService;
    private final ImageService imageService;

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return propertyService.getUserIdByEmail(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Get Landlord Dashboard Metrics",
            description = "Retrieves key metrics for a landlord's dashboard, such as total properties, active listings, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard metrics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LandlordDashboardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Requires LANDLORD or ADMIN role")
    })
    @GetMapping("/dashboard/metrics")
    public ResponseEntity<LandlordDashboardResponse> getLandlordDashboardMetrics() {
        UUID landlordId = getCurrentUserId();
        return ResponseEntity.ok(landlordService.getLandlordDashboardMetrics(landlordId));
    }

    @Operation(summary = "Get all properties owned by the current landlord",
            description = "Retrieves a list of all properties associated with the authenticated landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved properties",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))))
    })
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyResponse>> getLandlordProperties() {
        UUID landlordId = getCurrentUserId();
        return ResponseEntity.ok(landlordService.getLandlordProperties(landlordId));
    }

    @Operation(summary = "Get a specific property owned by the current landlord",
            description = "Retrieves details of a single property identified by ID, ensuring it belongs to the authenticated landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Property not owned by landlord"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @GetMapping("/properties/{id}")
    public ResponseEntity<PropertyResponse> getLandlordPropertyById(
            @Parameter(description = "Unique ID of the property", required = true)
            @PathVariable UUID id) {
        UUID landlordId = getCurrentUserId();
        return ResponseEntity.ok(landlordService.getLandlordPropertyById(id, landlordId));
    }

    @Operation(summary = "Create a new property listing",
            description = "Creates a new property listing associated with the authenticated landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Property created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid property data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/properties")
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest request) {
        UUID landlordId = getCurrentUserId();
        PropertyResponse newProperty = propertyService.createProperty(request, landlordId);
        return new ResponseEntity<>(newProperty, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing property listing",
            description = "Updates the details of an existing property listing owned by the authenticated landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid property data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Property not owned by landlord"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PutMapping("/properties/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @Parameter(description = "Unique ID of the property to update", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody PropertyRequest request) {
        UUID landlordId = getCurrentUserId();
        PropertyResponse updatedProperty = propertyService.updateProperty(id, request, landlordId);
        return ResponseEntity.ok(updatedProperty);
    }

    @Operation(summary = "Delete a property listing",
            description = "Deletes a property listing owned by the authenticated landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Property deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Property not owned by landlord"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @DeleteMapping("/properties/{id}")
    public ResponseEntity<Void> deleteProperty(
            @Parameter(description = "Unique ID of the property to delete", required = true)
            @PathVariable UUID id) {
        UUID landlordId = getCurrentUserId();
        propertyService.deleteProperty(id, landlordId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update property pricing",
            description = "Updates the pricing details for a specific property owned by the landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PropertyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid pricing data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Property not owned by landlord"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PutMapping("/properties/{id}/pricing")
    public ResponseEntity<PropertyResponse> updatePropertyPricing(
            @Parameter(description = "Unique ID of the property to update pricing for", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody PricingUpdateRequest request) {
        UUID landlordId = getCurrentUserId();
        PropertyResponse updatedProperty = landlordService.updatePropertyPricing(id, request, landlordId);
        return ResponseEntity.ok(updatedProperty);
    }

    @Operation(summary = "Upload property media (image)",
            description = "Uploads a new image for a specified property. The image will be associated with the property.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", format = "url", example = "/uploads/images/some_uuid_image.jpg"))),
            @ApiResponse(responseCode = "400", description = "Invalid file or upload error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Property not owned by landlord"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PostMapping(value = "/properties/{propertyId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPropertyMedia(
            @Parameter(description = "Unique ID of the property to associate the image with", required = true)
            @PathVariable UUID propertyId,
            @Parameter(description = "The image file to upload", required = true)
            @RequestParam("file") MultipartFile file) {
        // Note: The frontend might expect a URL as response, not a full ImageResponse object initially.
        // The service will handle linking it to the property.
        UUID landlordId = getCurrentUserId(); // Ensure landlord owns the property implicitly handled by ImageService logic
        String imageUrl = imageService.uploadPropertyImage(propertyId, file);
        return ResponseEntity.ok(imageUrl);
    }

    @Operation(summary = "Get property availability/bookings",
            description = "Retrieves booking details for a specific property owned by the landlord to show availability.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved bookings/availability",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class))))
    })
    @GetMapping("/properties/{propertyId}/availability")
    public ResponseEntity<List<BookingResponse>> getPropertyAvailability(
            @Parameter(description = "Unique ID of the property to check availability for", required = true)
            @PathVariable UUID propertyId) {
        UUID landlordId = getCurrentUserId();
        return ResponseEntity.ok(landlordService.getPropertyAvailability(propertyId, landlordId));
    }

    @Operation(summary = "Get incoming booking/rental requests for landlord's properties",
            description = "Retrieves all booking or rental requests submitted for properties owned by the authenticated landlord.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved requests",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BookingResponse.class))))
    })
    @GetMapping("/requests")
    public ResponseEntity<List<BookingResponse>> getBookingRequests() {
        UUID landlordId = getCurrentUserId();
        return ResponseEntity.ok(landlordService.getBookingRequests(landlordId));
    }
}
