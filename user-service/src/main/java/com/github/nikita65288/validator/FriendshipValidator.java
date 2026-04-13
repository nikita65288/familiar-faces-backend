package com.github.nikita65288.validator;

import com.github.nikita65288.entity.Friendship;
import com.github.nikita65288.enums.FriendshipStatus;
import com.github.nikita65288.exception.FFBadRequestException;
import com.github.nikita65288.exception.FFForbiddenException;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendshipValidator {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public FriendshipValidator(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Validate that addressee profile exists and requester is not sending to themselves.
     */
    public void validateSendRequest(Long requesterId, Long addresseeId) {
        if (addresseeId == null) {
            throw new FFBadRequestException("addresseeId must not be null");
        }
        if (requesterId.equals(addresseeId)) {
            throw new FFBadRequestException("Cannot send a friend request to yourself");
        }
        userProfileRepository.findByAuthId(addresseeId)
                .orElseThrow(() -> new FFNotFoundException("Addressee profile not found"));
    }

    /**
     * Validate that a request already exists between two users and handle status transitions.
     * Throws appropriate exception if transition is invalid.
     *
     * @param friendship existing friendship or null
     * @return true if friendship exists and can be re-used, false if new one should be created
     */
    public boolean validateExistingFriendship(Friendship friendship) {
        if (friendship == null) {
            return false; // Create new
        }

        return switch (friendship.getStatus()) {
            case PENDING -> throw new FFBadRequestException("Friend request already exists");
            case ACCEPTED -> throw new FFBadRequestException("Users are already friends");
            case REJECTED -> true; // Re-use after rejection
            default -> false;
        };
    }

    /**
     * Validate that only the requester can cancel a pending request.
     */
    public void validateCancelRequest(Friendship friendship, Long requesterId) {
        if (!friendship.getRequesterId().equals(requesterId)) {
            throw new FFForbiddenException("Only the requester can cancel the request");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new FFBadRequestException("Only pending requests can be cancelled");
        }
    }

    /**
     * Validate that only the addressee can accept a pending request.
     */
    public void validateAcceptRequest(Friendship friendship, Long addresseeId) {
        if (!friendship.getAddresseeId().equals(addresseeId)) {
            throw new FFForbiddenException("Only the addressee can accept the request");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new FFBadRequestException("Only pending requests can be accepted");
        }
    }

    /**
     * Validate that only the addressee can reject a pending request.
     */
    public void validateRejectRequest(Friendship friendship, Long addresseeId) {
        if (!friendship.getAddresseeId().equals(addresseeId)) {
            throw new FFForbiddenException("Only the addressee can reject the request");
        }
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new FFBadRequestException("Only pending requests can be rejected");
        }
    }

    /**
     * Validate that users are friends before removal.
     */
    public void validateRemoveFriend(Friendship friendship) {
        if (friendship.getStatus() != FriendshipStatus.ACCEPTED) {
            throw new FFBadRequestException("Users are not friends");
        }
    }
}