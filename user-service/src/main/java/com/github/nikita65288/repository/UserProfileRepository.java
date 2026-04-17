package com.github.nikita65288.repository;

import com.github.nikita65288.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByAuthId(Long authId);

    Optional<UserProfile> findByUsername(String username);

    @Query("select count(u) from UserProfile u where u.authId in ?1")
    long countByAuthIdIn(List<Long> authIds);

    void deleteByAuthId(Long authId);
}
