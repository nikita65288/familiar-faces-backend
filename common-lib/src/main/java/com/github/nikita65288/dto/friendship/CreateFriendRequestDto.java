package com.github.nikita65288.dto.friendship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateFriendRequestDto {

    private Long addresseeId;
}
