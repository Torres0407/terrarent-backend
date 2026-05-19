package com.terrarent.repository;

import com.terrarent.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Query("SELECT c FROM Conversation c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<Conversation> findByUser1IdOrUser2Id(UUID userId);

    Optional<Conversation> findByUser1IdAndUser2IdAndPropertyId(UUID user1Id, UUID user2Id, UUID propertyId);
    Optional<Conversation> findByUser1IdAndUser2Id(UUID user1Id, UUID user2Id);
}
