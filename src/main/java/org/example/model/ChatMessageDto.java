package org.example.model;

public class ChatMessageDto {
    private String sender;
    private String content;

    public ChatMessageDto(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }
}