package com.github.nikita65288.enums;

import com.github.nikita65288.exception.FFIllegalArgumentException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ParticipantRole {

    MEMBER(1),
    ADMIN(2);

    private final int id;
    ParticipantRole(int id) {
        this.id = id;
    }

    public static ParticipantRole getById(int id) {
        return Arrays.stream(ParticipantRole.values())
                .filter(participantRole -> participantRole.getId() == id)
                .findFirst()
                .orElseThrow(() -> new FFIllegalArgumentException("Unknown Participant Role ID: " + id));
    }
}
