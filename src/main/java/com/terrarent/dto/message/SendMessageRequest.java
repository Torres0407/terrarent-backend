package com.terrarent.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for sending a new message")
public class SendMessageRequest {
    @NotNull(message = "Recipient ID is required")
    @Schema(description = "Unique identifier of the recipient user", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID recipientId;

    @Schema(description = "Optional unique identifier of the property related to the conversation", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef")
    private UUID propertyId; // Optional, for messages related to a property

    @NotBlank(message = "Message content cannot be empty")
    @Schema(description = "Content of the message to send", example = "Hello, I'm interested in your property.")
    private String content;
}
