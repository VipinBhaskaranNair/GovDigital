package com.dge.chat.service;

import com.dge.chat.dto.CreateSessionRequest;
import com.dge.chat.dto.SessionDTO;

import java.util.List;

public interface ChatSessionService {
    SessionDTO createSession(CreateSessionRequest request);
    SessionDTO renameSession(String sessionId, String newTitle);
    void deleteSession(String sessionId);
    SessionDTO markFavorite(String sessionId, boolean favorite);
    List<SessionDTO> listSessions(String userId);
}
