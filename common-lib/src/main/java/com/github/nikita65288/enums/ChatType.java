package com.github.nikita65288.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.nikita65288.exception.FFIllegalArgumentException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChatType {

    PRIVATE(1),
    GROUP(2);

    @JsonValue
    private final int id;
    ChatType(int id) {
        this.id = id;
    }

    @JsonCreator
    public static ChatType getById(int id) {
        return Arrays.stream(ChatType.values())
                .filter(chatType -> chatType.getId() == id)
                .findFirst()
                .orElseThrow(() -> new FFIllegalArgumentException("Unknown ChatType ID: " + id));
    }
}
