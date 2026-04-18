package com.github.nikita65288.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chats.{chatId}.typing")
    public void handleTyping(
            @DestinationVariable Long chatId,
            @Payload Map<String, Object> payload
    ) {
        messagingTemplate.convertAndSend("/topic/chats." + chatId + ".typing", payload);
    }
}
