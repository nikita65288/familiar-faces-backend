package com.github.nikita65288.service;

import com.github.nikita65288.KafkaConstants;
import com.github.nikita65288.dto.event.MessageSentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationListener {

    @KafkaListener(
            topics = KafkaConstants.CHAT_MESSAGES_TOPIC,
            groupId = KafkaConstants.NOTIFICATION_GROUP
    )
    public void handleMessageSent(MessageSentEvent event) {
        log.info("==== NEW EVENT KAFKA ====");
        log.info("Received event from Kafka: Message {} in chat {}",
                event.getMessageId(), event.getChatId());

        if (event.getParticipantIds() != null) {
            // Simulate sending notifications to everyone except the sender
            event.getParticipantIds().stream()
                    .filter(userId -> !userId.equals(event.getSenderId()))
                    .forEach(userId -> {
                        log.info("SENDING NOTIFICATION to user ID {}: New text -> {}",
                                userId, event.getContent());
                    });
        }
    }
}
