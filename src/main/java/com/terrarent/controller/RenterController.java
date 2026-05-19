package com.terrarent.controller;

import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.renter.NegotiationRequest;
import com.terrarent.dto.renter.RenterDashboardResponse;
import com.terrarent.dto.renter.RentalApplicationRequest;
import com.terrarent.dto.renter.TourRequest;
import com.terrarent.entity.Application;
import com.terrarent.entity.Booking;
import com.terrarent.entity.Negotiation;
import com.terrarent.entity.Tour;
import com.terrarent.service.RenterService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/renter")
@RequiredArgsConstructor
@Tag(name = "Renter", description = "API for renter-specific operations")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("hasAnyAuthority('ROLE_RENTER', 'ROLE_ADMIN')") // All renter endpoints require these roles
public class RenterController {

    private final RenterService renterService;
    private final PropertyService propertyService; // Used for fetching user info implicitly

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return propertyService.getUserIdByEmail(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Get Renter Dashboard Metrics",
            description = "Retrieves key metrics for a renter's dashboard, such as saved properties count, bookings count, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved dashboard metrics",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterDashboardResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Requires RENTER or ADMIN role")
    })
    @GetMapping("/dashboard")
    public ResponseEntity<RenterDashboardResponse> getRenterDashboardMetrics() {
        UUID renterId = getCurrentUserId();
        return ResponseEntity.ok(renterService.getRenterDashboardMetrics(renterId));
    }

    @Operation(summary = "Get saved properties",
            description = "Retrieves a list of properties saved (favorited) by the authenticated renter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved saved properties",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PropertyResponse.class))))
    })
    @GetMapping("/saved-properties")
    public ResponseEntity<List<PropertyResponse>> getSavedProperties() {
        UUID renterId = getCurrentUserId();
        return ResponseEntity.ok(renterService.getSavedProperties(renterId));
    }

    @Operation(summary = "Save a property to favorites",
            description = "Adds a property to the authenticated renter's list of favorite properties.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property saved successfully"),
            @ApiResponse(responseCode = "400", description = "Property already saved"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PostMapping("/saved-properties/{propertyId}")
    public ResponseEntity<Void> saveProperty(
            @Parameter(description = "Unique ID of the property to save", required = true)
            @PathVariable UUID propertyId) {
        UUID renterId = getCurrentUserId();
        renterService.saveProperty(renterId, propertyId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unsave a property from favorites",
            description = "Removes a property from the authenticated renter's list of favorite properties.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Property unsaved successfully"),
            @ApiResponse(responseCode = "404", description = "Saved property not found")
    })
    @DeleteMapping("/saved-properties/{propertyId}")
    public ResponseEntity<Void> unsaveProperty(
            @Parameter(description = "Unique ID of the property to unsave", required = true)
            @PathVariable UUID propertyId) {
        UUID renterId = getCurrentUserId();
        renterService.unsaveProperty(renterId, propertyId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Submit a rental application",
            description = "Submits a new rental application for a property by the authenticated renter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application submitted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Application.class))),
            @ApiResponse(responseCode = "400", description = "Application already exists or invalid data"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PostMapping("/applications")
    public ResponseEntity<Application> createApplication(@Valid @RequestBody RentalApplicationRequest request) {
        UUID renterId = getCurrentUserId();
        Application application = renterService.createApplication(renterId, request);
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    @Operation(summary = "Create a property booking",
            description = "Creates a new property booking by the authenticated renter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "400", description = "Booking already exists or invalid data"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PostMapping("/bookings/{propertyId}")
    public ResponseEntity<Booking> createBooking(
            @Parameter(description = "Unique ID of the property to book", required = true)
            @PathVariable UUID propertyId) { // Simplified, actual booking will need a date, etc.
        UUID renterId = getCurrentUserId();
        Booking booking = renterService.createBooking(renterId, propertyId);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @Operation(summary = "Schedule a property tour",
            description = "Schedules a property tour for a specific property by the authenticated renter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tour scheduled successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tour.class))),
            @ApiResponse(responseCode = "400", description = "Tour already scheduled or invalid data"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    @PostMapping("/tours")
    public ResponseEntity<Tour> scheduleTour(@Valid @RequestBody TourRequest request) {
        UUID renterId = getCurrentUserId();
        Tour tour = renterService.scheduleTour(renterId, request);
        return new ResponseEntity<>(tour, HttpStatus.CREATED);
    }

    @Operation(summary = "Make a negotiation offer",
            description = "Submits a negotiation offer for a given application by the authenticated renter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Negotiation offer submitted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Negotiation.class))),
            @ApiResponse(responseCode = "400", description = "Negotiation already exists or invalid data"),
            @ApiResponse(responseCode = "404", description = "Application not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Renter not authorized for this application")
    })
    @PostMapping("/negotiations/{applicationId}/offers")
    public ResponseEntity<Negotiation> makeNegotiationOffer(
            @Parameter(description = "Unique ID of the application to make an offer for", required = true)
            @PathVariable UUID applicationId,
            @Valid @RequestBody NegotiationRequest request) {
        UUID renterId = getCurrentUserId();
        Negotiation negotiation = renterService.makeNegotiationOffer(renterId, applicationId, request);
        return new ResponseEntity<>(negotiation, HttpStatus.CREATED);
    }
}
