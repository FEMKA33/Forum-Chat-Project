package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<Long, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long discussionId = extractDiscussionId(session);
        sessions.computeIfAbsent(discussionId, id -> new CopyOnWriteArrayList<>()).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long discussionId = extractDiscussionId(session);

        // Парсим входящее сообщение
        Map<String, String> incoming = objectMapper.readValue(message.getPayload(), Map.class);

        // Формируем стандартный JSON для фронта
        Map<String, String> outgoing = new HashMap<>();
        outgoing.put("sender", "User"); // Можно заменить на авторизованного пользователя, если есть
        outgoing.put("content", incoming.get("content"));

        String json = objectMapper.writeValueAsString(outgoing);

        // Рассылаем всем участникам этой дискуссии
        for (WebSocketSession ws : sessions.get(discussionId)) {
            if (ws.isOpen()) {
                ws.sendMessage(new TextMessage(json));
            }
        }
    }

    private Long extractDiscussionId(WebSocketSession session) {
        String path = Objects.requireNonNull(session.getUri()).getPath();
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        return Long.parseLong(idStr);
    }
}