package org.example.repository;

import org.example.model.PostLike;
import org.example.model.Post;
import org.example.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    long countByPost(Post post);
    Optional<PostLike> findByPostAndUser(Post post, UserEntity user);
    void deleteByPostAndUser(Post post, UserEntity user);
}