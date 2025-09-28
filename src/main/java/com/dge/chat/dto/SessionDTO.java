package com.dge.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {
    private String id;
    private String title;
    private String userId;
    private boolean favorite;
    private Instant createdAt;
    private Instant updatedAt;
}
