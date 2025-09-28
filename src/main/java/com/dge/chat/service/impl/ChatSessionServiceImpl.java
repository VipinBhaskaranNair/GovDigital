package com.dge.chat.service.impl;

import com.dge.chat.dto.CreateSessionRequest;
import com.dge.chat.repository.ChatMessageRepository;
import com.dge.chat.dto.SessionDTO;
import com.dge.chat.entity.Session;
import com.dge.chat.repository.ChatSessionRepository;
import com.dge.chat.service.ChatSessionService;
import com.dge.chat.util.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionRepository sessionRepository;

    private final ChatMessageRepository messageRepository;

    public ChatSessionServiceImpl(ChatSessionRepository sessionRepository, ChatMessageRepository messageRepository) {
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public SessionDTO createSession(CreateSessionRequest request) {
        Session entity = MapperUtils.toEntity(request);
        Session savedObject = sessionRepository.save(entity);
        log.info("Session created for user Id: {}", request.getUserId());
        return MapperUtils.toDto(savedObject);
    }

    @Override
    public SessionDTO renameSession(String sessionId, String ownerId, String newTitle) {
        Session s = sessionRepository.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (!s.getUserId().equals(ownerId)) throw new SecurityException("Not owner");
        s.setTitle(newTitle);
        return MapperUtils.toDto(sessionRepository.save(s));
    }

    @Override
    public void deleteSession(String sessionId, String ownerId) {
        Session s = sessionRepository.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (!s.getUserId().equals(ownerId)) throw new SecurityException("Not owner");
        messageRepository.deleteBySessionId(sessionId);
        sessionRepository.delete(s);
    }

    @Override
    public SessionDTO markFavorite(String sessionId, String ownerId, boolean favorite) {
        Session s = sessionRepository.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (!s.getUserId().equals(ownerId)) throw new SecurityException("Not owner");
        s.setFavorite(favorite);
        return MapperUtils.toDto(sessionRepository.save(s));
    }

    @Override
    public List<SessionDTO> listSessions(String userId) {
        return sessionRepository.findByUserIdOrderByUpdatedAtDesc(userId).stream().map(MapperUtils::toDto).collect(Collectors.toList());
    }
}
