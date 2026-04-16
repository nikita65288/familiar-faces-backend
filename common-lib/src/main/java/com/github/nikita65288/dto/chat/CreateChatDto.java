package com.github.nikita65288.dto.chat;

import com.github.nikita65288.enums.ChatType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateChatDto {

    @Size(max = 120)
    private String name; // For group chats

    @NotNull(message = "Chat type is required (PRIVATE or GROUP)")
    private ChatType type;

    @NotEmpty(message = "The list of participant IDs cannot be empty")
    private List<Long> participantIds;

    @Size(max = 2000)
    private String firstMessage;
}
