package com.github.nikita65288.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/users/validate")
    boolean checkUsersExist(@RequestBody List<Long> userIds);
}
