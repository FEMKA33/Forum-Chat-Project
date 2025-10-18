package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository likeRepository;
    private final UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public Post create(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(Long postId, String text, String username) {
        Post post = postRepository.findById(postId).orElseThrow();
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        Comment c = Comment.builder()
                .text(text)
                .createdAt(LocalDateTime.now())
                .author(user)
                .post(post)
                .build();
        return commentRepository.save(c);
    }

    public long countLikes(Post post) {
        return likeRepository.countByPost(post);
    }

    @Transactional
    public long toggleLike(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow();
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        Optional<PostLike> opt = likeRepository.findByPostAndUser(post, user);
        if (opt.isPresent()) {
            likeRepository.delete(opt.get());
        } else {
            PostLike like = PostLike.builder().post(post).user(user).build();
            likeRepository.save(like);
        }
        return likeRepository.countByPost(post);
    }

    public List<Comment> getCommentsForPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
}