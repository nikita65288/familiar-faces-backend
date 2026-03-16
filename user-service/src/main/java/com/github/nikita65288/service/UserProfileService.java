package com.github.nikita65288.service;

import com.github.nikita65288.dto.user.UpdateUserProfileDto;
import com.github.nikita65288.dto.user.UserDto;
import com.github.nikita65288.dto.user.UserProfileDto;
import com.github.nikita65288.entity.UserProfile;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.mapper.UserMapper;
import com.github.nikita65288.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserProfileService(
            UserProfileRepository userProfileRepository,
            UserMapper userMapper
    ) {
        this.userProfileRepository = userProfileRepository;
        this.userMapper = userMapper;
    }

    @Cacheable(value = "userProfiles", key = "#authId")
    @Transactional(readOnly = true)
    public UserProfileDto getProfileByAuthId(Long authId) {
        UserProfile profile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new FFNotFoundException("Profile not found"));

        return userMapper.userProfileToUserProfileDto(profile);
    }

    @Transactional
    public void createInitialProfile(UserDto dto) {
        UserProfile profile = userMapper.userDtoToUserProfile(dto);
        userProfileRepository.save(profile);
    }

    @CachePut(value = "userProfiles", key = "#authId")
    @Transactional
    public UserProfileDto updateProfile(Long authId, UpdateUserProfileDto dto) {
        UserProfile profile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new FFNotFoundException("Profile not found"));

        userMapper.updateEntityFromDto(dto, profile);
        profile = userProfileRepository.save(profile);

        return userMapper.userProfileToUserProfileDto(profile);
    }

    @CachePut(value = "userProfiles", key = "#authId")
    @Transactional
    public UserProfileDto updateAvatar(Long authId, String avatarUrl) {
        UserProfile profile = userProfileRepository.findByAuthId(authId)
                .orElseThrow(() -> new FFNotFoundException("Profile not found"));

        profile.setAvatarUrl(avatarUrl);
        profile = userProfileRepository.save(profile);

        return userMapper.userProfileToUserProfileDto(profile);
    }

    @Transactional(readOnly = true)
    public boolean validateUsersExist(List<Long> authIds) {
        if (CollectionUtils.isEmpty(authIds)) {
            return false;
        }

        List<Long> uniqueIds = authIds.stream().distinct().toList();
        long foundCount = userProfileRepository.countByAuthIdIn(uniqueIds);

        return foundCount == uniqueIds.size();
    }

    @CacheEvict(value = "userProfiles", key = "#authId")
    @Transactional
    public void deleteUser(Long authId) {
        userProfileRepository.deleteByAuthId(authId);
    }
}