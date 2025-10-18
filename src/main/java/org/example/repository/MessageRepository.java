package org.example.repository;

import org.example.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByDiscussionIdOrderByCreatedAtAsc(Long discussionId);
}