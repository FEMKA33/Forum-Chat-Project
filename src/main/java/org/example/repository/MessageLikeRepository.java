package org.example.repository;

import org.example.model.MessageLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageLikeRepository extends JpaRepository<MessageLike, Long> {

    Optional<MessageLike> findByUserIdAndMessageId(Long userId, Long messageId);

    long countByMessageIdAndLiked(Long messageId, boolean liked);
}
