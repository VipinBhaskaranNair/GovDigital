package com.dge.chat.util;

import com.dge.chat.dto.MessageDTO;
import com.dge.chat.dto.SessionDTO;
import com.dge.chat.entity.Message;
import com.dge.chat.entity.Session;

public class MapperUtils {
    public static SessionDTO toDto(Session e) {
        if (e == null) return null;
        return SessionDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .userId(e.getUserId())
                .favorite(e.isFavorite())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static Session toEntity(SessionDTO d) {
        if (d == null) return null;
        return Session.builder()
                .id(d.getId() != null ? d.getId() : null)
                .title(d.getTitle())
                .ownerId(d.getUserId())
                .favorite(d.isFavorite())
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
