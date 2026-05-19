package com.terrarent.repository;

import com.terrarent.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TourRepository extends JpaRepository<Tour, UUID> {
    List<Tour> findByRenterId(UUID renterId);
    List<Tour> findByPropertyId(UUID propertyId);
    Optional<Tour> findByRenterIdAndPropertyIdAndTourDate(UUID renterId, UUID propertyId, LocalDateTime tourDate);
    long countByRenterId(UUID renterId);
}
