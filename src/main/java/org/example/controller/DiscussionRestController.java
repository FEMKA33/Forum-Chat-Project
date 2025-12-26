package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.ChatMessage;
import org.example.model.CreateDiscussionDto;
import org.example.model.Discussion;
import org.example.model.Message;
import org.example.model.OutgoingMessageDto;
import org.example.repository.MessageRepository;
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
    public final MessageRepository messageRepository;

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
        if (principal == null)
            throw new RuntimeException("Unauthorized");

        Message m = discussionService.addMessage(
                id,
                dto.getContent(),
                principal.getName(),
                dto.getParentId()
        );

        simp.convertAndSend("/topic/discussions/" + id,
                new OutgoingMessageDto(
                        m.getId(), m.getContent(), principal.getName(), m.getCreatedAt(), m.getParent() != null ? m.getParent().getId() : null
                )
        );

        return m;
    }

    @PostMapping("/messages/{id}/like")
    public void like(@PathVariable Long id, Principal principal) {
        if (principal == null)
            throw new RuntimeException("Unauthorized");

        discussionService.toggleLike(id, principal.getName(), true);
    }

    @PostMapping("/messages/{id}/dislike")
    public void dislike(@PathVariable Long id, Principal principal) {
        if (principal == null)
            throw new RuntimeException("Unauthorized");

        discussionService.toggleLike(id, principal.getName(), false);
    }
}