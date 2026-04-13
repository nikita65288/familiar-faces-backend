package com.github.nikita65288.dto.friendship;

import com.github.nikita65288.enums.FriendshipStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class FriendshipDto {

    private Long id;
    private Long requesterId;
    private Long addresseeId;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
