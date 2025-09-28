package com.dge.chat.service;

import com.dge.chat.dto.MessageDTO;
import com.dge.chat.entity.Message;
import com.dge.chat.entity.Session;
import com.dge.chat.repository.ChatMessageRepository;
import com.dge.chat.repository.ChatSessionRepository;
import com.dge.chat.service.impl.ChatMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatMessageServiceImplTest {

    @Mock
    private ChatMessageRepository messageRepo;

    @Mock
    private ChatSessionRepository sessionRepo;

    @InjectMocks
    private ChatMessageServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ChatMessageServiceImpl(messageRepo, sessionRepo);
    }

    @Test
    void addMessage_success() {
        MessageDTO dto = new MessageDTO();
        dto.setSessionId("s1");
        dto.setRole("user");
        dto.setContent("hello");

        when(sessionRepo.findById("s1")).thenReturn(Optional.of(new Session()));

        Message saved = Message.builder().id("m1").sessionId("s1").role("user").content("hello").build();
        when(messageRepo.save(any())).thenReturn(saved);

        MessageDTO result = service.addMessage(dto);

        assertNotNull(result);
        assertEquals("m1", result.getId());
        verify(messageRepo, times(1)).save(any());
    }

    @Test
    void getMessages_returnsPaged() {
        Message msg = Message.builder().id("m1").sessionId("s1").role("user").content("hello").build();
        Page<Message> page = new PageImpl<>(List.of(msg));
        when(messageRepo.findBySessionIdOrderByCreatedAtAsc(eq("s1"), any(PageRequest.class))).thenReturn(page);

        Page<?> res = service.getMessages("s1", PageRequest.of(0,10));
        assertNotNull(res);
        assertEquals(1, res.getTotalElements());
    }
}
