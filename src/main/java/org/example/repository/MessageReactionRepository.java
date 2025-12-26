package org.example.repository;

import org.example.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    Optional<MessageReaction> findByMessageAndUser(Message message, UserEntity user);
}
