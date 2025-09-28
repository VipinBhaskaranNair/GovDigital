package com.dge.chat.service;

import com.dge.chat.dto.CreateMessageRequest;
import com.dge.chat.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageService {
    MessageDTO addMessage(CreateMessageRequest createMessageRequest);
    Page<MessageDTO> getMessages(String sessionId, Pageable pageable);
}
