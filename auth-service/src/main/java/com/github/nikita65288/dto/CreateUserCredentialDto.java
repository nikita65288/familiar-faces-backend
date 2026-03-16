package com.github.nikita65288.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserCredentialDto {
    // TODO: add not null?
    private String username;
    private String email;
    private String password;
}
