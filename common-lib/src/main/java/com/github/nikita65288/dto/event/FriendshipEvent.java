package com.github.nikita65288.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipEvent {

    public enum Type {
        FRIEND_REQUEST_SENT,
        FRIEND_REQUEST_ACCEPTED
    }

    private Type type;
    private Long friendshipId;
    private Long initiatorId;
    private Long recipientId;
}
