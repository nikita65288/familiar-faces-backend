package com.github.nikita65288.dto.chat;

import com.github.nikita65288.enums.ChatType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatDto {
    private Long id;
    private String name;
    private ChatType type;
    private String avatarUrl;

    private List<Long> participantIds;
    private Long otherParticipantId;

    private String lastMessage;
    private LocalDateTime lastMessageAt;
    private Long lastMessageSenderId;

    private LocalDateTime createdAt;
}
