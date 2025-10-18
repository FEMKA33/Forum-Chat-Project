package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<Discussion> getAllDiscussions() {
        return discussionRepository.findAll();
    }

    public List<DiscussionDto> getAllDiscussionsDto() {
        return discussionRepository.findAll()
                .stream()
                .map(d -> new DiscussionDto(
                        d.getId(),
                        d.getTitle(),
                        d.getDescription(),
                        d.getAuthor(),
                        d.getCreatedAt()
                ))
                .toList();
    }

    public Discussion getDiscussion(Long id) {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Обсуждение не найдено: " + id));
    }

    @Transactional
    public Discussion createDiscussion(String title, String description, String username) {
        UserEntity author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        Discussion discussion = Discussion.builder()
                .title(title)
                .description(description)
                .createdAt(LocalDateTime.now())
                .author(author)
                .build();
        return discussionRepository.save(discussion);
    }

    @Transactional
    public Message addMessage(Long discussionId, String content, String username) {
        Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new IllegalArgumentException("Обсуждение не найдено: " + discussionId));
        UserEntity sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));

        Message message = Message.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .discussion(discussion)
                .sender(sender)
                .build();

        return messageRepository.save(message);
    }

    public Discussion findById(Long id) {
        return discussionRepository.findById(id).orElse(null);
    }

    public List<Message> getMessages(Long discussionId) {
        return messageRepository.findByDiscussionIdOrderByCreatedAtAsc(discussionId);
    }
}