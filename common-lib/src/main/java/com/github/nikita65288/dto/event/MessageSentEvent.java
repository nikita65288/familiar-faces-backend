package com.github.nikita65288.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSentEvent {
    private Long messageId;
    private Long chatId;
    private Long senderId;
    private String content;
    private List<Long> participantIds;
}
