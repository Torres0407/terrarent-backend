package com.terrarent.dto.message;

import com.terrarent.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for a message in a conversation")
public class MessageResponse {
    @Schema(description = "Unique identifier of the message", example = "g1h2i3j4-k5l6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "Details of the user who sent the message")
    private UserResponse sender;
    @Schema(description = "Content of the message")
    private String content;
    @Schema(description = "Timestamp when the message was sent", example = "2026-01-17T10:05:00")
    private LocalDateTime timestamp;
}
