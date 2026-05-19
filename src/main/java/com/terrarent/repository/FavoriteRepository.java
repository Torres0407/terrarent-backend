package com.terrarent.repository;

import com.terrarent.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByRenterId(UUID renterId);
    Optional<Favorite> findByRenterIdAndPropertyId(UUID renterId, UUID propertyId);
    boolean existsByRenterIdAndPropertyId(UUID renterId, UUID propertyId);
    long countByRenterId(UUID renterId);
}
