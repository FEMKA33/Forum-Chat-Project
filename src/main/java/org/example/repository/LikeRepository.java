package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.model.Like;
import org.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUsername(Post post, String username);
    int countByPost(Post post);

    @Transactional
    void deleteByPostAndUsername(Post post, String username);

    Optional<Like> findByPostAndUsername(Post post, String username);

    boolean existsByPostIdAndUsername(Long id, String name);
}