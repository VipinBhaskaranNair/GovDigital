package com.dge.chat.service.impl;

import com.dge.chat.dto.CreateMessageRequest;
import com.dge.chat.dto.MessageDTO;
import com.dge.chat.entity.Message;
import com.dge.chat.entity.Session;
import com.dge.chat.repository.ChatMessageRepository;
import com.dge.chat.repository.ChatSessionRepository;
import com.dge.chat.service.ChatMessageService;
import com.dge.chat.util.MapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository messageRepo;
    private final ChatSessionRepository sessionRepo;

    public ChatMessageServiceImpl(ChatMessageRepository messageRepo, ChatSessionRepository sessionRepo) {
        this.messageRepo = messageRepo;
        this.sessionRepo = sessionRepo;
    }

    @Override
    public MessageDTO addMessage(CreateMessageRequest createMessageRequest) {
        Session session = sessionRepo.findBySessionId(createMessageRequest.getSessionId());
        if (session == null) {
            throw new IllegalArgumentException("Session not found");
        }
        Message savedObject = messageRepo.save(MapperUtils.toEntity(createMessageRequest));
        return MapperUtils.toDto(savedObject);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessages(String sessionId, Pageable pageable) {
        return messageRepo.findBySessionIdOrderByCreatedAtAsc(sessionId, pageable).map(MapperUtils::toDto);
    }
}
