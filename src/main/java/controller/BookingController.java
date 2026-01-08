package controller;

import model.response.BookingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create booking - RENTER
    @PostMapping
    @PreAuthorize("hasRole('RENTER')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@RequestBody BookingRequest request,
                                         @RequestHeader("X-User-Id") String renterId) {
        return bookingService.createBooking(renterId, request);
    }

    // List bookings for renter - RENTER
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('RENTER')")
    public List<BookingResponse> getMyBookings(@RequestHeader("X-User-Id") String renterId) {
        return bookingService.getBookingsForRenter(renterId);
    }

    // Update booking status - LANDLORD
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LANDLORD')")
    public BookingResponse updateBookingStatus(@PathVariable Long id,
                                               @RequestBody BookingStatusUpdateRequest request) {
        return bookingService.updateBookingStatus(id, request);
    }
}
