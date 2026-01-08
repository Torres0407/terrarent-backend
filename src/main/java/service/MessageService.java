package service;

import model.entity.Message;
import model.response.MessageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import repository.MessageRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageResponse sendMessage(Authentication auth, MessageRequest request) {
        UUID senderId = UUID.fromString(auth.getName());

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }

        if (senderId.equals(request.getRecipientId())) {
            throw new IllegalArgumentException("Cannot send message to self");
        }

        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(request.getRecipientId());
        message.setContent(request.getContent());
        message.setBookingId(request.getBookingId());

        Message saved = messageRepository.save(message);

        return map(saved);
    }

    public List<MessageResponse> getMessages(Authentication auth, UUID otherUserId) {
        UUID userId = UUID.fromString(auth.getName());

        return messageRepository
                .findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByCreatedAtAsc(
                        userId,
                        otherUserId,
                        userId,
                        otherUserId
                )
                .stream()
                .map(this::map)
                .toList();
    }

    private MessageResponse map(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getRecipientId(),
                message.getContent(),
                message.getBookingId(),
                message.getCreatedAt()
        );
    }
}
