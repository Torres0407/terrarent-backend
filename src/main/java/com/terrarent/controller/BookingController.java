package com.terrarent.controller;

import com.terrarent.dto.booking.BookingResponse;
import com.terrarent.dto.booking.CreateBookingRequest;
import com.terrarent.service.BookingService;
import com.terrarent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "API for managing property bookings")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("hasAnyAuthority('ROLE_RENTER', 'ROLE_ADMIN')") // Only renters or admins can create bookings
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

//    private final PropertyService propertyService;

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userService.getUserIdByEmail(userDetails.getUsername());
        }

        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Create a new property booking",
            description = "Allows an authenticated renter to create a new booking for a specified property.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking data or booking already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden: Requires RENTER or ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Property or Renter not found")
    })
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        UUID renterId = getCurrentUserId();
        BookingResponse newBooking = bookingService.createBooking(renterId, request);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }
}
