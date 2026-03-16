package com.github.nikita65288.mapper;

import com.github.nikita65288.dto.user.UpdateUserProfileDto;
import com.github.nikita65288.dto.user.UserDto;
import com.github.nikita65288.dto.user.UserProfileDto;
import com.github.nikita65288.entity.UserProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Use in registration of UserProfile (UserDto -> UserProfile)
     */
    UserProfile userDtoToUserProfile(UserDto dto);

    /**
     * Use while updating existent UserProfile (UpdateUserProfileDto -> existent UserProfile)
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateUserProfileDto dto, @MappingTarget UserProfile entity);

    UserProfileDto userProfileToUserProfileDto(UserProfile userProfile);
}
