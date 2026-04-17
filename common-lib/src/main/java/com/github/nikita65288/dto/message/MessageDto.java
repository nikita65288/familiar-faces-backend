package com.github.nikita65288.dto.message;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class MessageDto {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;
    private String attachmentUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Map<String, List<Long>> reactions;
}
