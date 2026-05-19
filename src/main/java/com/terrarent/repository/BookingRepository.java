package com.terrarent.repository;

import com.terrarent.entity.Booking;
import com.terrarent.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByPropertyId(UUID propertyId);
    List<Booking> findByRenterId(UUID renterId);
    Optional<Booking> findByPropertyIdAndRenterIdAndBookingDate(UUID propertyId, UUID renterId, LocalDate bookingDate);
    List<Booking> findByPropertyLandlordId(UUID landlordId); // Get bookings for a landlord's properties
    long countByRenterIdAndStatus(UUID renterId, BookingStatus status);
}
