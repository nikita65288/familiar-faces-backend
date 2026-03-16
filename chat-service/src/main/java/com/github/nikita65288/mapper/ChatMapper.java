package com.github.nikita65288.mapper;

import com.github.nikita65288.BaseMapperConfig;
import com.github.nikita65288.dto.chat.ChatDto;
import com.github.nikita65288.dto.chat.CreateChatDto;
import com.github.nikita65288.entity.Chat;
import com.github.nikita65288.enums.ChatType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = BaseMapperConfig.class, imports = {ChatType.class})
public interface ChatMapper {

    ChatDto chatToChatDto(Chat chat);

    List<ChatDto> chatsToChatDtoList(List<Chat> chats);

    Chat createChatDtoToChat(CreateChatDto dto);

    @Mapping(target = "type", expression = "java(ChatType.GROUP)")
    Chat chatNameToGroupChat(String name);
}
