package com.dge.chat.service;

import com.dge.chat.dto.SessionDTO;
import com.dge.chat.entity.Session;
import com.dge.chat.repository.ChatMessageRepository;
import com.dge.chat.repository.ChatSessionRepository;
import com.dge.chat.service.impl.ChatSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatSessionServiceImplTest {

    @Mock
    private ChatSessionRepository sessionRepo;

    @Mock
    private ChatMessageRepository messageRepo;

    @InjectMocks
    private ChatSessionServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ChatSessionServiceImpl(sessionRepo, messageRepo);
    }

    @Test
    void createSession_savesAndReturnsDto() {
        SessionDTO dto = new SessionDTO();
        dto.setTitle("Test Session");
        dto.setUserId("user1");

        Session saved = Session.builder().id("s1").title("Test Session").userId("user1").build();
        when(sessionRepo.save(any())).thenReturn(saved);

        SessionDTO result = service.createSession(dto);

        assertNotNull(result);
        assertEquals("s1", result.getId());
        assertEquals("Test Session", result.getTitle());
        verify(sessionRepo, times(1)).save(any());
    }

    @Test
    void deleteSession_deletesMessagesAndSession() {
        Session s = Session.builder().id("s1").ownerId("user1").build();
        when(sessionRepo.findById("s1")).thenReturn(Optional.of(s));

        service.deleteSession("s1", "user1");

        verify(messageRepo, times(1)).deleteBySessionId("s1");
        verify(sessionRepo, times(1)).delete(s);
    }

    @Test
    void renameSession_failsIfNotOwner() {
        Session s = Session.builder().id("s1").ownerId("user1").title("old").build();
        when(sessionRepo.findById("s1")).thenReturn(Optional.of(s));

        SecurityException ex = assertThrows(SecurityException.class, () -> service.renameSession("s1", "otherUser", "new"));
        assertEquals("Not owner", ex.getMessage());
    }
}
