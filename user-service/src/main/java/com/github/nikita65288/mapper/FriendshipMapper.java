package com.github.nikita65288.mapper;

import com.github.nikita65288.dto.friendship.FriendshipDto;
import com.github.nikita65288.entity.Friendship;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    FriendshipDto friendshipToFriendshipDto(Friendship friendship);

    List<FriendshipDto> friendshipsToFriendshipDtoList(List<Friendship> friendships);
}
