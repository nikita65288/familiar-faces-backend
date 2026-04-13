package com.github.nikita65288.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.github.nikita65288.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByChatId(Long chatId, Pageable pageable);

    @Query("SELECT m.id FROM Message m WHERE m.chatId = :chatId AND m.senderId <> :userId AND m.isRead = false")
    List<Long> findUnreadMessageIds(Long chatId, Long userId);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true " +
            "WHERE m.chatId = :chatId AND m.senderId <> :userId AND m.isRead = false")
    void markMessagesAsRead(Long chatId, Long userId);
}
