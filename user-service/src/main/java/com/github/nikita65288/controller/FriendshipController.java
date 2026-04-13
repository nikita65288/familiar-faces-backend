package com.github.nikita65288.controller;

import com.github.nikita65288.dto.friendship.CreateFriendRequestDto;
import com.github.nikita65288.dto.friendship.FriendshipDto;
import com.github.nikita65288.service.FriendshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/requests")
    public ResponseEntity<FriendshipDto> sendRequest(
            @RequestBody CreateFriendRequestDto dto,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("User {} sends friend request to {}", authId, dto.getAddresseeId());
        FriendshipDto result = friendshipService.sendRequest(authId, dto.getAddresseeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/requests/{friendshipId}")
    public ResponseEntity<Void> cancelRequest(
            @PathVariable Long friendshipId,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("User {} cancels friend request {}", authId, friendshipId);
        friendshipService.cancelRequest(authId, friendshipId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/requests/{friendshipId}/accept")
    public ResponseEntity<FriendshipDto> acceptRequest(
            @PathVariable Long friendshipId,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("User {} accepts friend request {}", authId, friendshipId);
        return ResponseEntity.ok(friendshipService.acceptRequest(authId, friendshipId));
    }

    @PostMapping("/requests/{friendshipId}/reject")
    public ResponseEntity<FriendshipDto> rejectRequest(
            @PathVariable Long friendshipId,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("User {} rejects friend request {}", authId, friendshipId);
        return ResponseEntity.ok(friendshipService.rejectRequest(authId, friendshipId));
    }

    @GetMapping
    public ResponseEntity<List<FriendshipDto>> getFriends(@RequestHeader("X-User-Id") Long authId) {
        return ResponseEntity.ok(friendshipService.getFriends(authId));
    }

    @GetMapping("/requests/incoming")
    public ResponseEntity<List<FriendshipDto>> getIncomingRequests(@RequestHeader("X-User-Id") Long authId) {
        return ResponseEntity.ok(friendshipService.getIncomingRequests(authId));
    }

    @GetMapping("/requests/outgoing")
    public ResponseEntity<List<FriendshipDto>> getOutgoingRequests(@RequestHeader("X-User-Id") Long authId) {
        return ResponseEntity.ok(friendshipService.getOutgoingRequests(authId));
    }

    @DeleteMapping("/{otherUserAuthId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable Long otherUserAuthId,
            @RequestHeader("X-User-Id") Long authId
    ) {
        log.info("User {} removes friend {}", authId, otherUserAuthId);
        friendshipService.removeFriend(authId, otherUserAuthId);
        return ResponseEntity.noContent().build();
    }
}
