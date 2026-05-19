package com.terrarent.controller;

import com.terrarent.dto.bookingcom.BookingSearchRequest;
import com.terrarent.service.BookingComService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking-com")
@RequiredArgsConstructor
@Tag(name = "Booking.com Affiliate API", description = "Endpoints for interacting with live Booking.com hotel data")
public class BookingComController {

    private final BookingComService bookingComService;

    @Operation(summary = "Search for accommodations via Booking.com API")
    @PostMapping("/search")
    public ResponseEntity<String> searchHotels(@RequestBody BookingSearchRequest request) {
        // Here we just return the raw JSON from Booking.com.
        // You could also create a robust Response DTO to map the response cleanly.
        String response = bookingComService.searchAccommodations(request);
        return ResponseEntity.ok(response);
    }
}
