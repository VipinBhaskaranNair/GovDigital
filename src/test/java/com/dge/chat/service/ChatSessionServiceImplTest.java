package com.dge.chat.service;

import com.dge.chat.dto.CreateSessionRequest;
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

import java.time.Instant;
import java.util.List;
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
    private Session session;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ChatSessionServiceImpl(sessionRepo, messageRepo);
        session = Session.builder()
                .id("123")
                .title("Test Session")
                .userId("user-1")
                .favorite(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    void createSession_savesAndReturnsDto() {
        SessionDTO dto = new SessionDTO();
        dto.setTitle("Test Session");
        dto.setUserId("user1");
        CreateSessionRequest request = CreateSessionRequest.builder().title("Test Session").userId("user1").build();

        Session saved = Session.builder().id("s1").title("Test Session").userId("user1").build();
        when(sessionRepo.save(any())).thenReturn(saved);

        SessionDTO result = service.createSession(request);

        assertNotNull(result);
        assertEquals("s1", result.getId());
        assertEquals("Test Session", result.getTitle());
        verify(sessionRepo, times(1)).save(any());
    }

    @Test
    void deleteSession_deletesMessagesAndSession() {
        Session s = Session.builder().id("s1").userId("user1").build();
        when(sessionRepo.findById("s1")).thenReturn(Optional.of(s));

        service.deleteSession("s1");

        verify(messageRepo, times(1)).deleteBySessionId("s1");
        verify(sessionRepo, times(1)).delete(s);
    }

    @Test
    void testCreateSession() {
        CreateSessionRequest request = new CreateSessionRequest("user-1", "New Session", true);
        when(sessionRepo.save(any(Session.class))).thenReturn(session);

        SessionDTO dto = service.createSession(request);

        assertNotNull(dto);
        assertEquals("123", dto.getId());
        verify(sessionRepo).save(any(Session.class));
    }

    @Test
    void testRenameSession() {
        when(sessionRepo.findById("123")).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(Session.class))).thenReturn(session);

        SessionDTO dto = service.renameSession("123", "Renamed");

        assertEquals("Renamed", dto.getTitle());
        verify(sessionRepo).save(session);
    }

    @Test
    void testRenameSession_NotFound() {
        when(sessionRepo.findById("999")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.renameSession("999", "New Title"));
    }

    @Test
    void testDeleteSession() {
        when(sessionRepo.findById("123")).thenReturn(Optional.of(session));

        service.deleteSession("123");

        verify(messageRepo).deleteBySessionId("123");
        verify(sessionRepo).delete(session);
    }

    @Test
    void testDeleteSession_NotFound() {
        when(sessionRepo.findById("999")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.deleteSession("999"));
    }

    @Test
    void testMarkFavoriteTrue() {
        when(sessionRepo.findById("123")).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(Session.class))).thenReturn(session);

        SessionDTO dto = service.markFavorite("123", true);

        assertTrue(dto.isFavorite());
        verify(sessionRepo).save(session);
    }

    @Test
    void testMarkFavoriteFalse() {
        session.setFavorite(true);
        when(sessionRepo.findById("123")).thenReturn(Optional.of(session));
        when(sessionRepo.save(any(Session.class))).thenReturn(session);

        SessionDTO dto = service.markFavorite("123", false);

        assertFalse(dto.isFavorite());
        verify(sessionRepo).save(session);
    }

    @Test
    void testMarkFavorite_NotFound() {
        when(sessionRepo.findById("999")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.markFavorite("999", true));
    }

    @Test
    void testListSessions() {
        when(sessionRepo.findByUserIdOrderByUpdatedAtDesc("user-1"))
                .thenReturn(List.of(session));

        List<SessionDTO> result = service.listSessions("user-1");

        assertEquals(1, result.size());
        assertEquals("123", result.get(0).getId());
        verify(sessionRepo).findByUserIdOrderByUpdatedAtDesc("user-1");
    }

}
