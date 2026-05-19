package com.terrarent.repository;

import com.terrarent.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByConversationIdOrderByTimestampAsc(UUID conversationId);
    List<Message> findBySenderIdOrderByTimestampDesc(UUID senderId); // For getAllMessages if needed
}
