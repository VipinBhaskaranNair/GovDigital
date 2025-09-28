package com.dge.chat.util;

import com.dge.chat.dto.CreateMessageRequest;
import com.dge.chat.dto.CreateSessionRequest;
import com.dge.chat.dto.MessageDTO;
import com.dge.chat.dto.SessionDTO;
import com.dge.chat.entity.Message;
import com.dge.chat.entity.Session;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.UUID;

public class MapperUtils {
    public static SessionDTO toDto(Session session) {
        if (session == null) {
            return null;
        }
        return SessionDTO.builder()
                .id(session.getId())
                .title(session.getTitle())
                .userId(session.getUserId())
                .sessionId(session.getSessionId())
                .favorite(session.isFavorite())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

    public static Session toEntity(CreateSessionRequest request) {
        if (request == null)
        {
            return null;
        }
        return Session.builder()
                .id(UUID.randomUUID().toString())
                .title(request.getTitle())
                .userId(request.getUserId())
                .favorite(request.isFavorite())
                .sessionId(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static MessageDTO toDto(Message e) {
        if (e == null) {
            return null;
        }
        return MessageDTO.builder()
                .id(e.getId())
                .sessionId(e.getSessionId())
                .content(e.getContent())
                .context(e.getContext())
                .createdAt(e.getCreatedAt())
                .build();
    }

    public static Message toEntity(CreateMessageRequest createMessageRequest) {
        if (createMessageRequest == null) {
            return null;
        }
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .sessionId(createMessageRequest.getSessionId())
                .content(createMessageRequest.getContent())
                .createdAt(Instant.now())
                .build();
    }
}
