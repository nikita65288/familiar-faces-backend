package com.github.nikita65288.repository;

import com.github.nikita65288.entity.Chat;
import com.github.nikita65288.enums.ChatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c JOIN ChatParticipant cp ON c.id = cp.chatId WHERE cp.userId = :userId")
    List<Chat> findAllByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT c FROM Chat c 
        WHERE c.type = :type 
        AND c.id IN (SELECT cp1.chatId FROM ChatParticipant cp1 WHERE cp1.userId = :userId1)
        AND c.id IN (SELECT cp2.chatId FROM ChatParticipant cp2 WHERE cp2.userId = :userId2)
    """)
    Optional<Chat> findChatBetweenUsers(
            Long userId1,
            Long userId2,
            ChatType type
    );

    @Query(value = """
    SELECT c.* FROM chats c
    WHERE c.type_id = 1
    AND EXISTS (
        SELECT 1 FROM chat_participants cp
        WHERE cp.chat_id = c.id AND cp.user_id = :userId
    )
    AND (SELECT COUNT(*) FROM chat_participants cp2 WHERE cp2.chat_id = c.id) = 1
    """, nativeQuery = true)
    Optional<Chat> findSelfChatByUserId(@Param("userId") Long userId);

}
