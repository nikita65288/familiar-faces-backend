package com.github.nikita65288.service;

import com.github.nikita65288.KafkaConstants;
import com.github.nikita65288.dto.event.FriendshipEvent;
import com.github.nikita65288.dto.friendship.FriendshipDto;
import com.github.nikita65288.entity.Friendship;
import com.github.nikita65288.enums.FriendshipStatus;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.mapper.FriendshipMapper;
import com.github.nikita65288.repository.FriendshipRepository;
import com.github.nikita65288.validator.FriendshipValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    private final FriendshipValidator friendshipValidator;
    private final KafkaTemplate<String, FriendshipEvent> kafkaTemplate;

    @Autowired
    public FriendshipService(
            FriendshipRepository friendshipRepository,
            FriendshipMapper friendshipMapper,
            FriendshipValidator friendshipValidator,
            KafkaTemplate<String, FriendshipEvent> kafkaTemplate
    ) {
        this.friendshipRepository = friendshipRepository;
        this.friendshipMapper = friendshipMapper;
        this.friendshipValidator = friendshipValidator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public FriendshipDto sendRequest(Long requesterId, Long addresseeId) {
        friendshipValidator.validateSendRequest(requesterId, addresseeId);

        Friendship friendship = friendshipRepository.findBetween(requesterId, addresseeId)
                .map(existing -> {
                    boolean canReuse = friendshipValidator.validateExistingFriendship(existing);
                    if (canReuse) {
                        // re-use the row after rejection and reset to PENDING
                        existing.setRequesterId(requesterId);
                        existing.setAddresseeId(addresseeId);
                        existing.setStatus(FriendshipStatus.PENDING);
                    }
                    return existing;
                })
                .orElseGet(() -> Friendship.builder()
                        .requesterId(requesterId)
                        .addresseeId(addresseeId)
                        .status(FriendshipStatus.PENDING)
                        .build());

        friendship = friendshipRepository.save(friendship);

        publishEvent(FriendshipEvent.Type.FRIEND_REQUEST_SENT, friendship, requesterId, addresseeId);

        return friendshipMapper.friendshipToFriendshipDto(friendship);
    }

    @Transactional
    public void cancelRequest(Long requesterId, Long friendshipId) {
        Friendship friendship = getOrThrow(friendshipId);
        friendshipValidator.validateCancelRequest(friendship, requesterId);
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public FriendshipDto acceptRequest(Long addresseeId, Long friendshipId) {
        Friendship friendship = getOrThrow(friendshipId);
        friendshipValidator.validateAcceptRequest(friendship, addresseeId);

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship = friendshipRepository.save(friendship);

        publishEvent(
                FriendshipEvent.Type.FRIEND_REQUEST_ACCEPTED,
                friendship,
                addresseeId,
                friendship.getRequesterId()
        );

        return friendshipMapper.friendshipToFriendshipDto(friendship);
    }

    @Transactional
    public FriendshipDto rejectRequest(Long addresseeId, Long friendshipId) {
        Friendship friendship = getOrThrow(friendshipId);
        friendshipValidator.validateRejectRequest(friendship, addresseeId);

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendship = friendshipRepository.save(friendship);

        return friendshipMapper.friendshipToFriendshipDto(friendship);
    }

    @Transactional
    public void removeFriend(Long userId, Long otherUserId) {
        Friendship friendship = friendshipRepository.findBetween(userId, otherUserId)
                .orElseThrow(() -> new FFNotFoundException("Friendship not found"));
        friendshipValidator.validateRemoveFriend(friendship);
        friendshipRepository.delete(friendship);
    }

    @Transactional(readOnly = true)
    public List<FriendshipDto> getFriends(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllByUserIdAndStatus(
                userId, FriendshipStatus.ACCEPTED
        );
        return friendshipMapper.friendshipsToFriendshipDtoList(friendships);
    }

    @Transactional(readOnly = true)
    public List<FriendshipDto> getIncomingRequests(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllByAddresseeIdAndStatus(
                userId, FriendshipStatus.PENDING
        );
        return friendshipMapper.friendshipsToFriendshipDtoList(friendships);
    }

    @Transactional(readOnly = true)
    public List<FriendshipDto> getOutgoingRequests(Long userId) {
        List<Friendship> friendships = friendshipRepository.findAllByRequesterIdAndStatus(
                userId, FriendshipStatus.PENDING
        );
        return friendshipMapper.friendshipsToFriendshipDtoList(friendships);
    }

    //region private methods
    private Friendship getOrThrow(Long friendshipId) {
        return friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FFNotFoundException("Friendship not found"));
    }

    private void publishEvent(FriendshipEvent.Type type,
                              Friendship friendship,
                              Long initiatorId,
                              Long recipientId
    ) {
        FriendshipEvent event = FriendshipEvent.builder()
                .type(type)
                .friendshipId(friendship.getId())
                .initiatorId(initiatorId)
                .recipientId(recipientId)
                .build();
        kafkaTemplate.send(KafkaConstants.FRIENDSHIP_EVENTS_TOPIC, event);
    }
    //endregion private methods
}
