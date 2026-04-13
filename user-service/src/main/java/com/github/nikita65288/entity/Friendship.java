package com.github.nikita65288.entity;

import com.github.nikita65288.base_entity.UpdatableEntity;
import com.github.nikita65288.enums.FriendshipStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "friendships",
        uniqueConstraints = @UniqueConstraint(
                name = "friendships_requester_addressee_uk",
                columnNames = {"requester_id", "addressee_id"}
        )
)
public class Friendship extends UpdatableEntity {

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "addressee_id", nullable = false)
    private Long addresseeId;

    @Column(name = "status_id", nullable = false)
    private FriendshipStatus status;
}
