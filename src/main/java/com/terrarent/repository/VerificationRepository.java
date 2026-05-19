package com.terrarent.repository;

import com.terrarent.entity.Verification;
import com.terrarent.entity.Verification.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    Optional<Verification> findByUserId(UUID userId);
    List<Verification> findByStatus(VerificationStatus status);
    long countByStatus(VerificationStatus status);
}
