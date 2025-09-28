package com.dge.chat.service;

import com.dge.chat.dto.CreateSessionRequest;
import com.dge.chat.dto.SessionDTO;

import java.util.List;

public interface ChatSessionService {
    SessionDTO createSession(CreateSessionRequest request);
    SessionDTO renameSession(String sessionId, String ownerId, String newTitle);
    void deleteSession(String sessionId, String ownerId);
    SessionDTO markFavorite(String sessionId, String ownerId, boolean favorite);
    List<SessionDTO> listSessions(String ownerId);
}
