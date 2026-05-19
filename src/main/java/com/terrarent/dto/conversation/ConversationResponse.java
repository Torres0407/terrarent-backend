package com.terrarent.dto.conversation;

import com.terrarent.dto.property.PropertyResponse;
import com.terrarent.dto.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for a conversation")
public class ConversationResponse {
    @Schema(description = "Unique identifier of the conversation", example = "h1i2j3k4-l5m6-7890-1234-567890abcdef")
    private UUID id;
    @Schema(description = "First participant in the conversation")
    private UserResponse user1;
    @Schema(description = "Second participant in the conversation")
    private UserResponse user2;
    @Schema(description = "Property associated with the conversation (can be null for general chats)")
    private PropertyResponse property; // Can be null for general conversations
}
