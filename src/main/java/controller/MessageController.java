package controller;

import model.request.MessageRequest;
import model.response.MessageResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import service.MessageService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@PreAuthorize("isAuthenticated()")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageResponse sendMessage(
            Authentication authentication,
            @RequestBody MessageRequest request
    ) {
        return messageService.sendMessage(authentication, request);
    }

    @GetMapping
    public List<MessageResponse> getMessages(
            Authentication authentication,
            @RequestParam UUID userId
    ) {
        return messageService.getMessages(authentication, userId);
    }
}
