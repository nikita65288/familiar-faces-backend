package com.github.nikita65288.service;

import com.github.nikita65288.KafkaConstants;
import com.github.nikita65288.dto.chat.ChatDto;
import com.github.nikita65288.dto.chat.CreateChatDto;
import com.github.nikita65288.dto.event.MessageSentEvent;
import com.github.nikita65288.dto.message.CreateMessageDto;
import com.github.nikita65288.dto.message.MessageDto;
import com.github.nikita65288.entity.Chat;
import com.github.nikita65288.entity.ChatParticipant;
import com.github.nikita65288.entity.Message;
import com.github.nikita65288.enums.ChatType;
import com.github.nikita65288.enums.ParticipantRole;
import com.github.nikita65288.mapper.ChatMapper;
import com.github.nikita65288.mapper.MessageMapper;
import com.github.nikita65288.repository.ChatParticipantRepository;
import com.github.nikita65288.repository.ChatRepository;
import com.github.nikita65288.repository.MessageRepository;
import com.github.nikita65288.validator.ChatValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatService {

    // Templates
    private final KafkaTemplate<String, MessageSentEvent> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    // Repositories
    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MessageRepository messageRepository;

    // Mappers
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;

    // Validators
    private final ChatValidator chatValidator;

    @Autowired
    public ChatService(
            // Templates
            KafkaTemplate<String, MessageSentEvent> kafkaTemplate,
            SimpMessagingTemplate messagingTemplate,
            // Repositories
            ChatRepository chatRepository,
            ChatParticipantRepository chatParticipantRepository,
            MessageRepository messageRepository,
            // Mappers
            ChatMapper chatMapper,
            MessageMapper messageMapper,
            // Validators
            ChatValidator chatValidator
    ) {
        // Templates
        this.kafkaTemplate = kafkaTemplate;
        this.messagingTemplate = messagingTemplate;
        // Repositories
        this.chatRepository = chatRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.messageRepository = messageRepository;
        // Mappers
        this.chatMapper = chatMapper;
        this.messageMapper = messageMapper;
        // Validators
        this.chatValidator = chatValidator;
    }

    @Transactional(readOnly = true)
    public List<ChatDto> getUserChats(Long userId) {
        List<Chat> chats = chatRepository.findAllByUserId(userId);
        return chatMapper.chatsToChatDtoList(chats);
    }

    @Transactional
    public Page<MessageDto> getChatHistory(Long chatId, Long userId, int page, int size) {

        chatValidator.validateParticipant(chatId, userId);

        markMessagesAsReadAndNotify(chatId, userId);

        // TODO: refactoring needed?
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Message> messagePage = messageRepository.findByChatId(chatId, pageable);

        List<MessageDto> content = new ArrayList<>(
                messagePage.getContent().stream()
                        .map(messageMapper::messageToMessageDto)
                        .toList()
        );

        Collections.reverse(content);

        return new PageImpl<>(content, pageable, messagePage.getTotalElements());
    }

    @Transactional
    public ChatDto startConversation(Long creatorId, CreateChatDto createChatDto) {

        // Clear the list of participants from the creator and duplicates
        List<Long> otherParticipants = createChatDto.getParticipantIds().stream()
                .filter(id -> !id.equals(creatorId))
                .distinct()
                .toList();

        // Validate data
        chatValidator.validateChatCreation(creatorId, otherParticipants, createChatDto);

        // Search for an existing private chat
        if (createChatDto.getType() == ChatType.PRIVATE) {
            Optional<Chat> existingChat = chatRepository.findChatBetweenUsers(creatorId, otherParticipants.get(0), ChatType.PRIVATE);
            if (existingChat.isPresent()) {
                Chat chat = existingChat.get();

                // Send message from createChatDto to existent chat
                sendFirstMessageIfExists(chat.getId(), creatorId, createChatDto.getFirstMessage());
                return chatMapper.chatToChatDto(chat);
            }
        }

        // Create chat
        Chat chat = chatMapper.createChatDtoToChat(createChatDto);
        chat = chatRepository.save(chat);
        Long chatId = chat.getId();

        // Add chat creator (ADMIN)
        addParticipant(chatId, creatorId, ParticipantRole.ADMIN);

        // Add chat participants (MEMBER)
        otherParticipants.forEach(pId -> addParticipant(chatId, pId, ParticipantRole.MEMBER));

        // If there is the first message, then create and send it
        sendFirstMessageIfExists(chat.getId(), creatorId, createChatDto.getFirstMessage());

        return chatMapper.chatToChatDto(chat);
    }

    @Transactional
    public MessageDto saveMessage(Long chatId, Long senderId, CreateMessageDto createMessageDto) {

        chatValidator.validateParticipant(chatId, senderId);

        markMessagesAsReadAndNotify(chatId, senderId);

        Message message = messageMapper.createMessageDtoToMessage(chatId, senderId, createMessageDto);
        message = messageRepository.save(message);

        MessageDto messageDto = messageMapper.messageToMessageDto(message);

        // Get chat participants for Kafka
        List<Long> participantIds = chatParticipantRepository.findAllByChatId(chatId)
                .stream()
                .map(ChatParticipant::getUserId)
                .toList();

        // Send the saved message to all subscribers of this chat
        // Clients must be subscribed to /topic/chats.{chatId}
        messagingTemplate.convertAndSend(
                "/topic/chats." + chatId,
                messageDto
        );

        // Kafka: Asynchronous notification
        MessageSentEvent event = MessageSentEvent.builder()
                .messageId(messageDto.getId())
                .chatId(chatId)
                .senderId(senderId)
                .content(messageDto.getContent())
                .participantIds(participantIds)
                .build();
        kafkaTemplate.send(KafkaConstants.CHAT_MESSAGES_TOPIC, event);

        return messageDto;
    }

    @Transactional
    public void markMessagesAsReadAndNotify(Long chatId, Long userId) {
        // Get the IDs of messages that will be marked as read
        List<Long> newlyReadMessageIds = messageRepository.findUnreadMessageIds(chatId, userId);

        // Mark them as read
        if (!newlyReadMessageIds.isEmpty()) {
            messageRepository.markMessagesAsRead(chatId, userId);

            System.out.println("Sending read notification to /topic/chats." + chatId + ".read with messageIds: " + newlyReadMessageIds);

            // Notify via WebSocket
            messagingTemplate.convertAndSend(
                    "/topic/chats." + chatId + ".read",
                    Map.of(
                            "readerId", userId,
                            "messageIds", newlyReadMessageIds,
                            "chatId", chatId
                    )
            );
        }
    }

    @Transactional
    public void deleteMessage(Long chatId, Long messageId, Long userId) {

        // Validate
        chatValidator.validateParticipant(chatId, userId);
        chatValidator.validateMessageAuthorship(messageId, userId);

        messageRepository.deleteById(messageId);

        // Notify everyone via WebSocket
        messagingTemplate.convertAndSend(
                "/topic/chats." + chatId + ".delete",
                messageId
        );
    }

    //region private methods
    private void addParticipant(Long chatId, Long userId, ParticipantRole role) {
        ChatParticipant participant = new ChatParticipant();
        participant.setChatId(chatId);
        participant.setUserId(userId);
        participant.setRole(role);
        participant.setJoinedAt(LocalDateTime.now());
        chatParticipantRepository.save(participant);
    }

    private void sendFirstMessageIfExists(Long chatId, Long senderId, String text) {
        if (StringUtils.hasText(text)) {
            CreateMessageDto msgDto = new CreateMessageDto();
            msgDto.setContent(text);

            saveMessage(chatId, senderId, msgDto);
        }
    }
    //endregion private methods
}
