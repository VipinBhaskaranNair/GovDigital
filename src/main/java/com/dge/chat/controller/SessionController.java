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

    @GetMapping
    public ResponseEntity<ApiResponse<List<SessionDTO>>> list(Principal principal) {
        String owner = principal != null ? principal.getName() : "demo-user";
        return ResponseEntity.ok(ApiResponse.success(service.listSessions(owner)));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<ApiResponse<SessionDTO>> rename(@PathVariable String id, @RequestParam String title, Principal principal) {
        String owner = principal != null ? principal.getName() : "demo-user";
        return ResponseEntity.ok(ApiResponse.success(service.renameSession(id, owner, title)));
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<SessionDTO>> favorite(@PathVariable String id, @RequestParam boolean favorite, Principal principal) {
        String owner = principal != null ? principal.getName() : "demo-user";
        return ResponseEntity.ok(ApiResponse.success(service.markFavorite(id, owner, favorite)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id, Principal principal) {
        String owner = principal != null ? principal.getName() : "demo-user";
        service.deleteSession(id, owner);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
