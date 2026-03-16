package com.github.nikita65288.validator;

import com.github.nikita65288.client.UserClient;
import com.github.nikita65288.dto.chat.CreateChatDto;
import com.github.nikita65288.entity.Message;
import com.github.nikita65288.enums.ChatType;
import com.github.nikita65288.exception.FFBadRequestException;
import com.github.nikita65288.exception.FFForbiddenException;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.exception.FFRuntimeException;
import com.github.nikita65288.repository.ChatParticipantRepository;
import com.github.nikita65288.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatValidator {

    private final UserClient userClient;

    private final ChatParticipantRepository chatParticipantRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatValidator(
            UserClient userClient,
            ChatParticipantRepository chatParticipantRepository,
            MessageRepository messageRepository
    ) {
        this.userClient = userClient;
        this.chatParticipantRepository = chatParticipantRepository;
        this.messageRepository = messageRepository;
    }

    public void validateChatCreation(Long creatorId, List<Long> otherParticipants, CreateChatDto createChatDto) {

        if (otherParticipants == null || otherParticipants.isEmpty()) {
            throw new FFBadRequestException("Chat cannot be created without participants.");
        }

        if (createChatDto.getType() == ChatType.PRIVATE) {
            if (otherParticipants.size() > 1) {
                throw new FFBadRequestException("There can only be one interlocutor in a private chat.");
            }

            if (!StringUtils.hasText(createChatDto.getFirstMessage())) {
                throw new FFBadRequestException("To create a private chat, you need to send the first message..");
            }
        }

        if (createChatDto.getType() == ChatType.GROUP) {
            if (!StringUtils.hasText(createChatDto.getName())) {
                throw new FFBadRequestException("A group chat must have a name.");
            }
        }

        // Checking the existence of users via user-service
        // Send all chat participants (creator + others) in 1 request
        List<Long> allChatParticipantUserIds = new ArrayList<>(otherParticipants);
        allChatParticipantUserIds.add(creatorId);

        boolean usersExist;
        try {
            usersExist = userClient.checkUsersExist(allChatParticipantUserIds);
        } catch (Exception e) {
            throw new RuntimeException("Unable to verify users. The service is temporarily unavailable.", e);
        }

        if (!usersExist) {
            throw new FFBadRequestException("One or more specified users were not found in the system.");
        }
    }

    /**
     * Checks if the user is a member of the chat
     */
    public void validateParticipant(Long chatId, Long userId) {
        if (!chatParticipantRepository.existsByChatIdAndUserId(chatId, userId)) {
            throw new FFForbiddenException("User " + userId + " does not have access to chat " + chatId);
        }
    }

    public void validateMessageAuthorship(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new FFNotFoundException("Message not found with id: " + messageId));

        if (!message.getSenderId().equals(userId)) {
            throw new FFForbiddenException("You can only delete your own messages");
        }
    }
}

