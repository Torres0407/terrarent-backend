package com.terrarent.service;

import com.terrarent.dto.conversation.ConversationResponse;
import com.terrarent.dto.message.MessageResponse;
import com.terrarent.entity.Conversation;
import com.terrarent.entity.Message;
import com.terrarent.entity.Property;
import com.terrarent.entity.User;
import com.terrarent.exception.ResourceNotFoundException;
import com.terrarent.repository.ConversationRepository;
import com.terrarent.repository.MessageRepository;
import com.terrarent.repository.PropertyRepository;
import com.terrarent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyService propertyService; // Reuse for property mapping

    public List<ConversationResponse> getUserConversations(UUID userId) {
        return conversationRepository.findByUser1IdOrUser2Id(userId).stream()
                .map(this::mapConversationToConversationResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getConversationMessages(UUID conversationId, UUID userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + conversationId));

        // Ensure the current user is a participant in the conversation
        if (!conversation.getUser1().getId().equals(userId) && !conversation.getUser2().getId().equals(userId)) {
            throw new ResourceNotFoundException("User not a participant in conversation with id: " + conversationId);
        }

        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId).stream()
                .map(this::mapMessageToMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Conversation getOrCreateConversation(UUID user1Id, UUID user2Id, Optional<UUID> propertyId) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user1Id));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user2Id));

        // Ensure a consistent order for user1 and user2 to avoid duplicate conversations
        UUID actualUser1Id = user1Id.compareTo(user2Id) < 0 ? user1Id : user2Id;
        UUID actualUser2Id = user1Id.compareTo(user2Id) < 0 ? user2Id : user1Id;
        User actualUser1 = user1Id.compareTo(user2Id) < 0 ? user1 : user2;
        User actualUser2 = user1Id.compareTo(user2Id) < 0 ? user2 : user1;

        Optional<Conversation> existingConversation;
        if (propertyId.isPresent()) {
            existingConversation = conversationRepository.findByUser1IdAndUser2IdAndPropertyId(actualUser1Id, actualUser2Id, propertyId.get());
        } else {
            existingConversation = conversationRepository.findByUser1IdAndUser2Id(actualUser1Id, actualUser2Id);
        }

        if (existingConversation.isPresent()) {
            return existingConversation.get();
        }

        Property property = propertyId.map(pId -> propertyRepository.findById(pId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + pId)))
                .orElse(null);

        Conversation newConversation = Conversation.builder()
                .user1(actualUser1)
                .user2(actualUser2)
                .property(property)
                .build();
        return conversationRepository.save(newConversation);
    }

    private ConversationResponse mapConversationToConversationResponse(Conversation conversation) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .user1(UserService.mapUserToUserResponse(conversation.getUser1()))
                .user2(UserService.mapUserToUserResponse(conversation.getUser2()))
                .property(conversation.getProperty() != null ? propertyService.mapPropertyToPropertyResponse(conversation.getProperty()) : null)
                .build();
    }

    private MessageResponse mapMessageToMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(UserService.mapUserToUserResponse(message.getSender()))
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
}
