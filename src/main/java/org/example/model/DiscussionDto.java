package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiscussionDto {
    private Long id;
    private String title;
    private String description;
    private UserEntity author;
    private LocalDateTime createdAt;

    public DiscussionDto(Long id, String title, String description, UserEntity author, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.createdAt = createdAt;
    }
}