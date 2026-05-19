package com.terrarent.repository;

import com.terrarent.entity.Negotiation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NegotiationRepository extends JpaRepository<Negotiation, UUID> {
    Optional<Negotiation> findByApplicationId(UUID applicationId);
}
