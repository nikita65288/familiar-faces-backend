package com.github.nikita65288.controller;

import com.github.nikita65288.dto.media.AvatarUrlDto;
import com.github.nikita65288.dto.user.UpdateUserProfileDto;
import com.github.nikita65288.dto.user.UserDto;
import com.github.nikita65288.dto.user.UserProfileDto;
import com.github.nikita65288.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    public void createUser(@RequestBody UserDto userDto) {
        userProfileService.createInitialProfile(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile(
            @RequestHeader("X-User-Id") Long authId
    ) {
        return ResponseEntity.ok(userProfileService.getProfileByAuthId(authId));
    }

    @GetMapping("/{authId}")
    public ResponseEntity<UserProfileDto> getUser(@PathVariable Long authId) {
        return ResponseEntity.ok(userProfileService.getProfileByAuthId(authId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateMyProfile(
            @RequestBody UpdateUserProfileDto dto,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("Updating the profile of a user with ID: {}", authId);
        return ResponseEntity.ok(userProfileService.updateProfile(authId, dto));
    }

    @PatchMapping("/me/avatar")
    public ResponseEntity<UserProfileDto> updateMyAvatar(
            @RequestBody AvatarUrlDto dto,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("Updating the avatar for a user with ID: {}", authId);
        return ResponseEntity.ok(userProfileService.updateAvatar(authId, dto.getAvatarUrl()));
    }

    @PostMapping("/validate")
    public boolean validateUsers(@RequestBody List<Long> authIds) {
        return userProfileService.validateUsersExist(authIds);
    }
}
