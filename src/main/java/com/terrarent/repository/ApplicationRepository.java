package com.terrarent.repository;

import com.terrarent.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {
    List<Application> findByRenterId(UUID renterId);
    List<Application> findByPropertyLandlordId(UUID landlordId);
    Optional<Application> findByRenterIdAndPropertyId(UUID renterId, UUID propertyId);
    long countByRenterId(UUID renterId);
}
