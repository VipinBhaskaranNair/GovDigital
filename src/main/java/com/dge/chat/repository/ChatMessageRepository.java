package com.dge.chat.repository;

import com.dge.chat.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<Message, String> {
    void deleteBySessionId(String sessionId);

    Page<Message> findBySessionIdOrderByCreatedAtAsc(String sessionId, Pageable pageable);
}
