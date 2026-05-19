package com.terrarent.controller;

import com.terrarent.dto.conversation.ConversationResponse;
import com.terrarent.dto.message.MessageResponse;
import com.terrarent.service.ConversationService;
import com.terrarent.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
@Tag(name = "Messaging & Conversations", description = "API for managing user conversations and messages")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("isAuthenticated()") // All conversation endpoints require authentication
public class ConversationController {

    private final ConversationService conversationService;
    private final PropertyService propertyService; // Used for fetching user info implicitly

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return propertyService.getUserIdByEmail(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Get all conversations for the authenticated user",
            description = "Retrieves a list of all conversations the authenticated user is a part of.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved conversations",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ConversationResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getUserConversations() {
        UUID userId = getCurrentUserId();
        return ResponseEntity.ok(conversationService.getUserConversations(userId));
    }

    @Operation(summary = "Get messages within a specific conversation",
            description = "Retrieves all messages for a given conversation ID. Accessible only to participants of the conversation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    })
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<MessageResponse>> getConversationMessages(
            @Parameter(description = "Unique ID of the conversation", required = true)
            @PathVariable UUID conversationId) {
        UUID userId = getCurrentUserId();
        return ResponseEntity.ok(conversationService.getConversationMessages(conversationId, userId));
    }
}
