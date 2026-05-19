package com.terrarent.repository;

import com.terrarent.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByPropertyId(UUID propertyId);
    Optional<Review> findByPropertyIdAndRenterId(UUID propertyId, UUID renterId);
}
