package com.github.nikita65288.dto.chat;

import com.github.nikita65288.enums.ChatType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatDto {
    private Long id;
    private String name;
    private ChatType type;
    private LocalDateTime createdAt;
}
