package com.github.nikita65288.repository;

import com.github.nikita65288.entity.ChatParticipant;
import com.github.nikita65288.entity.ChatParticipantPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantPK> {

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    List<ChatParticipant> findAllByChatId(Long chatId);

    void deleteByChatIdAndUserId(Long chatId, Long userId);
}
