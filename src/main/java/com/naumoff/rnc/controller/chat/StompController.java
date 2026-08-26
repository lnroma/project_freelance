package com.naumoff.rnc.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.dto.chat.TriggerDto;
import com.naumoff.rnc.services.TemplateService;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.chat.MessageService;
import com.naumoff.rnc.services.chat.SaveMessageService;
import com.naumoff.rnc.services.stomp.ConversationUpdaterService;
import com.naumoff.rnc.services.stomp.SendMessageToConversationService;
import com.naumoff.rnc.services.stomp.StompService;
import com.naumoff.rnc.services.users.UserService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StompController {

    final private ConversationsService conversationsService;
    final private MessageService messageService;
    final private ConversationUpdaterService conversationUpdaterService;
    private final UserService userService;
    private final SendMessageToConversationService sendMessageToConversationService;
    private final SaveMessageService saveMessageService;

    public StompController(
            ConversationsService conversationsService,
            MessageService messageService,
            ConversationUpdaterService conversationUpdaterService,
            UserService userService,
            SendMessageToConversationService sendMessageToConversationService,
            SaveMessageService saveMessageService
    ) {
        this.conversationsService = conversationsService;
        this.messageService = messageService;
        this.conversationUpdaterService = conversationUpdaterService;
        this.userService = userService;
        this.sendMessageToConversationService = sendMessageToConversationService;
        this.saveMessageService = saveMessageService;
    }

    @MessageMapping("/send-message")
    public void sendMessage(
            ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Conversation conversation = this.conversationsService.getConversationWithUserOrCreateNew(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId()
        );

        saveMessageService.saveMessage(Long.valueOf(headerAccessor.getUser().getName()),conversation, chatMessage);

        conversationUpdaterService.updateConversationList(
                userService.getUserById(conversation.getUserFrom()),
                userService.getUserById(conversation.getUserTo()),
                headerAccessor.getMessageHeaders(),
                conversation
        );

        sendMessageToConversationService.sendMessages(headerAccessor, conversation.getId());
    }

    @MessageMapping("/load-message")
    public void triggerMessageGet(
            TriggerDto triggerDto,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        sendMessageToConversationService.sendMessages(headerAccessor, triggerDto.getConversationId());
    }
}
