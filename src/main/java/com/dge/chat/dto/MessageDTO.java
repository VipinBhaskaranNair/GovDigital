package com.dge.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;

@Data
public class MessageDTO {
    private String id;
    private String sessionId;
    @NotBlank
    private String role;
    @NotBlank
    private String content;
    private Instant createdAt;
    private String context;
}
