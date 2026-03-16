package com.github.nikita65288.entity;

import com.github.nikita65288.enums.ParticipantRole;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_participants")
@IdClass(ChatParticipantPK.class)
public class ChatParticipant {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private ParticipantRole role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
