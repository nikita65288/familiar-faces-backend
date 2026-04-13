package com.github.nikita65288.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.nikita65288.exception.FFIllegalArgumentException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FriendshipStatus {

    PENDING(1),
    ACCEPTED(2),
    REJECTED(3);

    @JsonValue
    private final int id;
    FriendshipStatus(int id) {
        this.id = id;
    }

    @JsonCreator
    public static FriendshipStatus getById(int id) {
        return Arrays.stream(FriendshipStatus.values())
                .filter(status -> status.getId() == id)
                .findFirst()
                .orElseThrow(() -> new FFIllegalArgumentException("Unknown FriendshipStatus ID: " + id));
    }
}
