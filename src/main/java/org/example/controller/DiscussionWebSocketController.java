package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.ChatMessage;
import org.example.model.Message;
import org.example.model.OutgoingMessageDto;
import org.example.service.DiscussionService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DiscussionWebSocketController {
    private final DiscussionService discussionService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/discussions/{id}/send")
    public void receiveMessage(@DestinationVariable Long id, ChatMessage msg, Principal principal) {
        String username = (principal != null) ? principal.getName() : msg.getSender();
        Message m = discussionService.addMessage(id, msg.getContent(), username, msg.getParentId());
        OutgoingMessageDto out = new OutgoingMessageDto(
                m.getId(),
                m.getContent(),
                m.getSender().getUsername(),
                m.getCreatedAt(),
                m.getParent() != null ? m.getParent().getId() : null
        );
        template.convertAndSend("/topic/discussions/" + id, out);
    }
}