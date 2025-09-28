package com.dge.chat.service.impl;

import com.dge.chat.repository.ChatMessageRepository;
import com.dge.chat.dto.SessionDTO;
import com.dge.chat.entity.Session;
import com.dge.chat.repository.ChatSessionRepository;
import com.dge.chat.service.ChatSessionService;
import com.dge.chat.util.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatSessionServiceImpl implements ChatSessionService {
    private final ChatSessionRepository repo;
    private final ChatMessageRepository messageRepo;

    public ChatSessionServiceImpl(ChatSessionRepository repo, ChatMessageRepository messageRepo) { this.repo = repo; this.messageRepo = messageRepo; }

    @Override
    public SessionDTO createSession(SessionDTO dto) {
        Session entity = MapperUtils.toEntity(dto);
        if (entity.getId() != null) entity.setId(entity.getId());
        Session saved = repo.save(entity);
        return MapperUtils.toDto(saved);
    }

    @Override
    public SessionDTO renameSession(String sessionId, String ownerId, String newTitle) {
        Session s = repo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (!s.getUserId().equals(ownerId)) throw new SecurityException("Not owner");
        s.setTitle(newTitle);
        return MapperUtils.toDto(repo.save(s));
    }

    @Override
    public void deleteSession(String sessionId, String ownerId) {
        Session s = repo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (!s.getUserId().equals(ownerId)) throw new SecurityException("Not owner");
        messageRepo.deleteBySessionId(sessionId);
        repo.delete(s);
    }

    @Override
    public SessionDTO markFavorite(String sessionId, String ownerId, boolean favorite) {
        Session s = repo.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        if (!s.getUserId().equals(ownerId)) throw new SecurityException("Not owner");
        s.setFavorite(favorite);
        return MapperUtils.toDto(repo.save(s));
    }

    @Override
    public List<SessionDTO> listSessions(String ownerId) {
        return repo.findByOwnerIdOrderByUpdatedAtDesc(ownerId).stream().map(MapperUtils::toDto).collect(Collectors.toList());
    }
}
