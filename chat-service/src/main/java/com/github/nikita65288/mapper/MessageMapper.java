package com.github.nikita65288.mapper;

import com.github.nikita65288.BaseMapperConfig;
import com.github.nikita65288.dto.message.CreateMessageDto;
import com.github.nikita65288.dto.message.MessageDto;
import com.github.nikita65288.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public interface MessageMapper {

    @Mapping(target = "isRead", expression = "java(false)")
    Message createMessageDtoToMessage(Long chatId, Long senderId, CreateMessageDto createMessageDto);

    MessageDto messageToMessageDto(Message message);
}
