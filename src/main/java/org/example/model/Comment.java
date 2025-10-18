package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}