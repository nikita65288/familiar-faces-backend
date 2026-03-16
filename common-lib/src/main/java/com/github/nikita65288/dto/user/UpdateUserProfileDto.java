package com.github.nikita65288.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileDto {
    private String firstName;
    private String lastName;
    private String bio;
}
