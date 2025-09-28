package com.dge.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CreateMessageRequest {
    @NotBlank
    private String sessionId;
    @NotBlank
    private String content;
    private String context;
}
