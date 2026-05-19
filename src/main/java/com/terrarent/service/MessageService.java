package com.terrarent.service;

import com.terrarent.dto.message.MessageResponse;
import com.terrarent.dto.message.SendMessageRequest;
import com.terrarent.entity.Conversation;
import com.terrarent.entity.Message;
import com.terrarent.entity.User;
import com.terrarent.exception.ResourceNotFoundException;
import com.terrarent.repository.MessageRepository;
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
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationService conversationService;

    @Transactional
    public MessageResponse sendMessage(UUID senderId, SendMessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found with id: " + senderId));
        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with id: " + request.getRecipientId()));

        // Get or create conversation between sender, recipient, and optional property
        Conversation conversation = conversationService.getOrCreateConversation(sender.getId(), recipient.getId(), Optional.ofNullable(request.getPropertyId()));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.getContent())
                .build();

        Message savedMessage = messageRepository.save(message);
        return mapMessageToMessageResponse(savedMessage);
    }

    public List<MessageResponse> getAllMessagesBySender(UUID senderId) {
        return messageRepository.findBySenderIdOrderByTimestampDesc(senderId).stream()
                .map(this::mapMessageToMessageResponse)
                .collect(Collectors.toList());
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
