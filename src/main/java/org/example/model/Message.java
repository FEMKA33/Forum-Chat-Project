package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Discussion discussion;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Message parent;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int likes = 0;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int dislikes = 0;
}