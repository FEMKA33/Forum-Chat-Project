package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.ChatMessage;
import org.example.model.CreateDiscussionDto;
import org.example.model.Discussion;
import org.example.model.Message;
import org.example.model.OutgoingMessageDto;
import org.example.service.DiscussionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
public class DiscussionRestController {

    private final DiscussionService discussionService;
    private final SimpMessagingTemplate simp;

    @GetMapping
    public List<Discussion> list() {
        return discussionService.getAllDiscussions();
    }

    @PostMapping
    public Discussion create(@RequestBody CreateDiscussionDto dto, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        return discussionService.createDiscussion(dto.getTitle(), dto.getDescription(), principal.getName());
    }

    @GetMapping("/{id}/messages")
    public List<Message> messages(@PathVariable Long id) {
        return discussionService.getMessages(id);
    }

    @PostMapping("/{id}/messages")
    public Message addMessage(@PathVariable Long id, @RequestBody ChatMessage dto, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }
        Message m = discussionService.addMessage(id, dto.getContent(), principal.getName());
        simp.convertAndSend("/topic/discussions/" + id,
                new OutgoingMessageDto(m.getId(), m.getContent(), principal.getName(), m.getCreatedAt()));
        return m;
    }
}