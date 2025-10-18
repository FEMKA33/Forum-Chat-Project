package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OutgoingMessageDto {
    private final Long id;
    private final String content;
    private final String sender;
    private final LocalDateTime createdAt;

    public OutgoingMessageDto(Long id, String content, String sender, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.createdAt = createdAt;
    }

}