package com.dge.chat.controller;

import com.dge.chat.dto.CreateSessionRequest;
import com.dge.chat.dto.SessionDTO;
import com.dge.chat.service.ChatSessionService;
import com.dge.chat.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@Slf4j
public class SessionController {
    private final ChatSessionService service;

    public SessionController(ChatSessionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<ApiResponse<SessionDTO>> create(@Valid @RequestBody CreateSessionRequest request) {
        log.info("Create session for user Id: {}", request.getUserId());
        SessionDTO createdSession = service.createSession(request);
        return ResponseEntity.ok(ApiResponse.success(createdSession));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<SessionDTO>>> list(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(ApiResponse.success(service.listSessions(userId)));
    }

    @PatchMapping("/{sessionId}/rename")
    public ResponseEntity<ApiResponse<SessionDTO>> rename(@PathVariable("sessionId") String sessionId, @RequestParam("title") String title) {
        return ResponseEntity.ok(ApiResponse.success(service.renameSession(sessionId, title)));
    }

    @PatchMapping("/{sessionId}/favorite")
    public ResponseEntity<ApiResponse<SessionDTO>> favorite(@PathVariable("sessionId") String sessionId, @RequestParam boolean favorite) {
        return ResponseEntity.ok(ApiResponse.success(service.markFavorite(sessionId, favorite)));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("sessionId") String sessionId) {
        service.deleteSession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
