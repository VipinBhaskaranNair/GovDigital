package com.dge.chat.service.impl;

import com.dge.chat.dto.MessageDTO;
import com.dge.chat.entity.Message;
import com.dge.chat.repository.ChatMessageRepository;
import com.dge.chat.repository.ChatSessionRepository;
import com.dge.chat.service.ChatMessageService;
import com.dge.chat.util.MapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository messageRepo;
    private final ChatSessionRepository sessionRepo;

    public ChatMessageServiceImpl(ChatMessageRepository messageRepo, ChatSessionRepository sessionRepo) {
        this.messageRepo = messageRepo; this.sessionRepo = sessionRepo;
    }

    @Override
    public MessageDTO addMessage(MessageDTO dto) {
        sessionRepo.findById(dto.getSessionId()).orElseThrow(() -> new IllegalArgumentException("Session not found"));
        Message e = MapperUtils.toEntity(dto);
        Message saved = messageRepo.save(e);
        return MapperUtils.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessages(String sessionId, Pageable pageable) {
        return messageRepo.findBySessionIdOrderByCreatedAtAsc(sessionId, pageable).map(MapperUtils::toDto);
    }
}
