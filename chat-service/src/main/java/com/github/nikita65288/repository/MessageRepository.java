package com.github.nikita65288.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.github.nikita65288.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByChatIdOrderByCreatedAtDesc(Long chatId, Pageable pageable);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true " +
            "WHERE m.chatId = :chatId AND m.senderId <> :userId AND m.isRead = false")
    void markMessagesAsRead(Long chatId, Long userId);
}
