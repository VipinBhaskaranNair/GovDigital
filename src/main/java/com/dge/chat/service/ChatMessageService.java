package com.dge.chat.service;

import com.dge.chat.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageService {
    MessageDTO addMessage(MessageDTO dto);
    Page<MessageDTO> getMessages(String sessionId, Pageable pageable);
}
