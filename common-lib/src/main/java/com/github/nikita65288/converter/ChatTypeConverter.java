package com.github.nikita65288.converter;

import com.github.nikita65288.enums.ChatType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ChatTypeConverter implements AttributeConverter<ChatType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ChatType attribute) {
        return attribute != null ? attribute.getId() : null;
    }

    @Override
    public ChatType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ChatType.getById(dbData) : null;
    }
}
