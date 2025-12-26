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
    public final MessageLikeRepository likeRepository;

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
    public Message addMessage(Long discussionId, String content, String username, Long parentId) {
        Discussion discussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new IllegalArgumentException("Обсуждение не найдено: " + discussionId));

        UserEntity sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));

        Message parent = null;
        if (parentId != null) {
            parent = messageRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("Родительское сообщение не найдено: " + parentId));
        }

        Message message = Message.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .discussion(discussion)
                .sender(sender)
                .parent(parent)
                .build();

        return messageRepository.save(message);
    }

    public List<DiscussionDto> searchDiscussions(String query) {
        return discussionRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(DiscussionDto::fromEntity)
                .toList();
    }

    @Transactional
    public void toggleLike(Long messageId, String username, boolean isLike) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        var existing = likeRepository.findByUserIdAndMessageId(user.getId(), messageId);

        if (existing.isPresent()) {
            MessageLike ml = existing.get();
            if (ml.isLiked() == isLike) {
                likeRepository.delete(ml);
            } else {
                ml.setLiked(isLike);
                likeRepository.save(ml);
            }
        } else {
            MessageLike newLike = MessageLike.builder()
                    .user(user)
                    .message(message)
                    .liked(isLike)
                    .build();
            likeRepository.save(newLike);
        }

        long likes = likeRepository.countByMessageIdAndLiked(messageId, true);
        long dislikes = likeRepository.countByMessageIdAndLiked(messageId, false);

        message.setLikes((int) likes);
        message.setDislikes((int) dislikes);
        messageRepository.save(message);
    }


    public Discussion findById(Long id) {
        return discussionRepository.findById(id).orElse(null);
    }

    public List<Message> getMessages(Long discussionId) {
        return messageRepository.findByDiscussionIdOrderByCreatedAtAsc(discussionId);
    }
}