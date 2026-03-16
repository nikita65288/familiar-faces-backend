package com.github.nikita65288.base_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class UpdatableEntity extends BaseEntity {

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
