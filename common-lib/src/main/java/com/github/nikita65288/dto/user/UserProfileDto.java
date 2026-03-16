package com.github.nikita65288.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserProfileDto {

    private Long id;
    private Long authId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarUrl;
}
