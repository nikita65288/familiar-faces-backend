package com.github.nikita65288.client;

import com.github.nikita65288.dto.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/users")
    void createUser(@RequestBody UserDto userDto);
}
