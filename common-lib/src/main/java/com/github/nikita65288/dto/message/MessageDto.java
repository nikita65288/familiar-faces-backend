package com.github.nikita65288.dto.message;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDto {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}
