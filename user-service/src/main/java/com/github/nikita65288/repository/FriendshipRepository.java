package com.github.nikita65288.repository;

import com.github.nikita65288.entity.Friendship;
import com.github.nikita65288.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("""
            select f from Friendship f
            where (f.requesterId = :a and f.addresseeId = :b)
               or (f.requesterId = :b and f.addresseeId = :a)
            """)
    Optional<Friendship> findBetween(@Param("a") Long userA, @Param("b") Long userB);

    @Query("""
            select f from Friendship f
            where f.status = :status
              and (f.requesterId = :userId or f.addresseeId = :userId)
            """)
    List<Friendship> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                              @Param("status") FriendshipStatus status);

    List<Friendship> findAllByAddresseeIdAndStatus(Long addresseeId, FriendshipStatus status);

    List<Friendship> findAllByRequesterIdAndStatus(Long requesterId, FriendshipStatus status);
}
