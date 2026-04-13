package com.github.nikita65288.converter;

import com.github.nikita65288.enums.FriendshipStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FriendshipStatusConverter implements AttributeConverter<FriendshipStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(FriendshipStatus attribute) {
        return attribute != null ? attribute.getId() : null;
    }

    @Override
    public FriendshipStatus convertToEntityAttribute(Integer dbData) {
        return dbData != null ? FriendshipStatus.getById(dbData) : null;
    }
}
