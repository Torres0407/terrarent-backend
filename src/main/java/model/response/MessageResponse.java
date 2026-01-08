package model.response;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class MessageResponse {

    private UUID id;
    private UUID senderId;
    private UUID recipientId;
    private String content;
    private UUID bookingId;
    private Instant createdAt;

    public MessageResponse(
            UUID id,
            UUID senderId,
            UUID recipientId,
            String content,
            UUID bookingId,
            Instant createdAt
    ) {
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.bookingId = bookingId;
        this.createdAt = createdAt;
    }
}
