package com.terrarent.controller;

import com.terrarent.dto.message.MessageResponse;
import com.terrarent.dto.message.SendMessageRequest;
import com.terrarent.service.MessageService;
import com.terrarent.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messaging & Conversations", description = "API for managing user conversations and messages")
@SecurityRequirement(name = "bearerAuth") // Apply JWT security to all endpoints in this controller
@PreAuthorize("isAuthenticated()") // All message endpoints require authentication
public class MessageController {

    private final MessageService messageService;
    private final PropertyService propertyService; // Used for fetching user info implicitly

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return propertyService.getUserIdByEmail(userDetails.getUsername());
        }
        throw new RuntimeException("User not authenticated.");
    }


    @Operation(summary = "Send a new message",
            description = "Sends a new message to a recipient, potentially linking it to a property.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message sent successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid message data or recipient/property not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        UUID senderId = getCurrentUserId();
        MessageResponse newMessage = messageService.sendMessage(senderId, request);
        return new ResponseEntity<>(newMessage, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all messages sent by the authenticated user",
            description = "Retrieves a list of all messages sent by the authenticated user across all conversations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MessageResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessagesBySender() {
        UUID senderId = getCurrentUserId();
        return ResponseEntity.ok(messageService.getAllMessagesBySender(senderId));
    }
}
