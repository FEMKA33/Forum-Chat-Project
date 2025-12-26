package org.example.repository;

import org.example.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
