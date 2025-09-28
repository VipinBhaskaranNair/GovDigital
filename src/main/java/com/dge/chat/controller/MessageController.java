package com.dge.chat.controller;

import com.dge.chat.dto.CreateMessageRequest;
import com.dge.chat.dto.MessageDTO;
import com.dge.chat.service.ChatMessageService;
import com.dge.chat.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
@Slf4j
public class MessageController {

    private final ChatMessageService chatMessageService;

    public MessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MessageDTO>> add(@Valid @RequestBody CreateMessageRequest createMessageRequest) {
        log.info("Adding message: {}", createMessageRequest);
        return ResponseEntity.ok(ApiResponse.success(chatMessageService.addMessage(createMessageRequest)));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<Page<MessageDTO>>> getMessages(@PathVariable String sessionId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "50") int size) {
        log.info("Getting messages for session: {}", sessionId);
        int safeSize = Math.min(size, 200); // cap page size to 200
        Page<MessageDTO> p = chatMessageService.getMessages(sessionId, PageRequest.of(page, safeSize));
        return ResponseEntity.ok(ApiResponse.success(p));
    }
}
