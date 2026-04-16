package com.github.nikita65288.entity;

import com.github.nikita65288.base_entity.BaseEntity;
import com.github.nikita65288.enums.ChatType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "chats")
public class Chat extends BaseEntity {

    @Column(name = "type_id")
    private ChatType type;

    @Column(name = "name")
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;
}
