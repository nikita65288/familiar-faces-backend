package com.github.nikita65288.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipantPK implements Serializable {
    private Long chatId;
    private Long userId;
}
