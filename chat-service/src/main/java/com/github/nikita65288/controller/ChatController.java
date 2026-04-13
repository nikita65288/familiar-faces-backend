package com.github.nikita65288.controller;

import com.github.nikita65288.dto.chat.ChatDto;
import com.github.nikita65288.dto.chat.CreateChatDto;
import com.github.nikita65288.dto.message.CreateMessageDto;
import com.github.nikita65288.dto.message.MessageDto;
import com.github.nikita65288.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.github.nikita65288.Consts.USER_ID_HEADER;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getMyChats(
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        System.out.println("User Id Header: " + userId);
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @PostMapping
    public ResponseEntity<ChatDto> createChat(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody CreateChatDto createChatDto
    ) {
        return ResponseEntity.ok(chatService.startConversation(userId, createChatDto));
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<Page<MessageDto>> getChatHistory(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestHeader(USER_ID_HEADER) Long currentUserId
    ) {
        Page<MessageDto> history = chatService.getChatHistory(chatId, currentUserId, page, size);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable Long chatId,
            @RequestHeader(USER_ID_HEADER) Long userId,
            @Valid @RequestBody CreateMessageDto dto
    ) {
        return ResponseEntity.ok(chatService.saveMessage(chatId, userId, dto));
    }

    @PostMapping("/{chatId}/read")
    public ResponseEntity<Void> markChatAsRead(
            @PathVariable Long chatId,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        chatService.markMessagesAsReadAndNotify(chatId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long chatId,
            @PathVariable Long messageId,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        chatService.deleteMessage(chatId, messageId, userId);
        return ResponseEntity.noContent().build();
    }
}
