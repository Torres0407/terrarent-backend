package com.terrarent.service;

import com.terrarent.dto.booking.BookingResponse;
import com.terrarent.dto.booking.CreateBookingRequest;
import com.terrarent.entity.Booking;
import com.terrarent.entity.Property;
import com.terrarent.entity.User;
import com.terrarent.exception.CustomAuthenticationException;
import com.terrarent.exception.ResourceNotFoundException;
import com.terrarent.repository.BookingRepository;
import com.terrarent.repository.PropertyRepository;
import com.terrarent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyService propertyService;

    @Transactional
    public BookingResponse createBooking(UUID renterId, CreateBookingRequest request) {
        User renter = userRepository.findById(renterId)
                .orElseThrow(() -> new ResourceNotFoundException("Renter not found with id: " + renterId));
        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + request.getPropertyId()));

        // Check for existing booking for the same property, renter, and date
        Optional<Booking> existingBooking = bookingRepository.findByPropertyIdAndRenterIdAndBookingDate(
                request.getPropertyId(), renterId, request.getBookingDate());
        if (existingBooking.isPresent()) {
            throw new CustomAuthenticationException("A booking already exists for this property and date by you.");
        }

        Booking booking = Booking.builder()
                .renter(renter)
                .property(property)
                .bookingDate(request.getBookingDate())
                .status(Booking.BookingStatus.PENDING)
                .build();
        // Additional logic for setting status based on business rules
        if (request.getStatus() != null) {
            try {
                booking.setStatus(Booking.BookingStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Optionally handle invalid status string, perhaps default to PENDING
                booking.setStatus(Booking.BookingStatus.PENDING);
            }
        }

        Booking savedBooking = bookingRepository.save(booking);
        return mapBookingToBookingResponse(savedBooking);
    }

    private BookingResponse mapBookingToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .property(propertyService.mapPropertyToPropertyResponse(booking.getProperty()))
                .renter(UserService.mapUserToUserResponse(booking.getRenter()))
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
