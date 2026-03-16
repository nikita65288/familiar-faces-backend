package com.github.nikita65288.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {

    private Long authId;
    private String username;
    private String email;
}
