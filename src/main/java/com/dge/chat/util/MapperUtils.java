package com.dge.chat.util;

import com.dge.chat.dto.CreateSessionRequest;
import com.dge.chat.dto.MessageDTO;
import com.dge.chat.dto.SessionDTO;
import com.dge.chat.entity.Message;
import com.dge.chat.entity.Session;
import org.apache.commons.lang3.StringUtils;

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
                .build();
    }

    public static MessageDTO toDto(Message e) {
        if (e == null) return null;
        MessageDTO d = new MessageDTO();
        d.setId(e.getId()); d.setSessionId(e.getSessionId()); d.setRole(e.getRole()); d.setContent(e.getContent()); d.setCreatedAt(e.getCreatedAt()); d.setContext(e.getContext());
        return d;
    }

    public static Message toEntity(MessageDTO d) {
        if (d == null) return null;
        return Message.builder()
                .id(d.getId())
                .sessionId(d.getSessionId())
                .role(d.getRole())
                .content(d.getContent())
                .build();
    }
}
