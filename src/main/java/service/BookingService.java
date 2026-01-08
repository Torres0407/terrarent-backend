package service;
import model.entity.Booking;
import model.request.Booking.BookingRequest;
import model.response.BookingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import repository.BookingRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public BookingResponse createBooking(String renterId, BookingRequest request) {
        // Check for date conflicts
        boolean conflict = bookingRepository.existsByPropertyIdAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                request.getPropertyId(),
                request.getCheckOutDate(),
                request.getCheckInDate()
        );

        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The selected dates are no longer available for this property.");
        }

        Booking booking = new Booking();
        booking.setPropertyId(request.getPropertyId());
        booking.setRenterId(renterId);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setPaymentMethod(request.getPaymentInfo().getMethod());
        booking.setPaymentToken(request.getPaymentInfo().getToken());

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    public List<BookingResponse> getBookingsForRenter(String renterId) {
        return bookingRepository.findByRenterId(renterId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse updateBookingStatus(Long bookingId, BookingStatusUpdateRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        booking.setStatus(request.getStatus());
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setStatus(booking.getStatus());
        response.setPropertyId(booking.getPropertyId());
        response.setRenterId(booking.getRenterId());
        response.setCreatedAt(booking.getCreatedAt());
        return response;
    }
}
