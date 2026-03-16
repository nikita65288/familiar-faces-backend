package com.github.nikita65288.converter;

import com.github.nikita65288.enums.ParticipantRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ParticipantRoleConverter implements AttributeConverter<ParticipantRole, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ParticipantRole attribute) {
        return attribute != null ? attribute.getId() : null;
    }

    @Override
    public ParticipantRole convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ParticipantRole.getById(dbData) : null;
    }
}
