package com.terrarent.repository;

import com.terrarent.entity.FeaturedProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeaturedPropertyRepository extends JpaRepository<FeaturedProperty, UUID> {
    Optional<FeaturedProperty> findByPropertyId(UUID propertyId);
    
    @Modifying
    @Query("DELETE FROM FeaturedProperty fp WHERE fp.property.id = :propertyId")
    void deleteByPropertyId(UUID propertyId);

    @Query("SELECT fp.property.id FROM FeaturedProperty fp ORDER BY fp.orderIndex ASC NULLS LAST, fp.featuredAt DESC")
    List<UUID> findFeaturedPropertyIdsOrdered();
}
