package repository;

import model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenterId(String renterId);

    boolean existsByPropertyIdAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            String propertyId, LocalDate checkOutDate, LocalDate checkInDate
    );
}