package com.dge.chat.repository;

import com.dge.chat.entity.Message;
import com.dge.chat.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatSessionRepository extends JpaRepository<Session, String> {
    List<Session> findByUserIdOrderByUpdatedAtDesc(String userId);

    Session findBySessionId(String sessionId);
}
