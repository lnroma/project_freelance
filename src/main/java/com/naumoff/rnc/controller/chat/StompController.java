package com.naumoff.rnc.controller.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.dto.chat.TriggerDto;
import com.naumoff.rnc.services.TemplateService;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.chat.ConversationsService;
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

    final private ChatService chatService;
    final private TemplateService templateService;
    final private SimpMessagingTemplate simpMessagingTemplate;
    final private ConversationRepository conversationRepository;
    final private ConversationMessageRepository conversationMessageRepository;
    final private ConversationsService conversationsService;

    public StompController(
            ChatService chatService,
            TemplateService templateService,
            SimpMessagingTemplate simpMessagingTemplate,
            ConversationRepository conversationRepository,
            ConversationMessageRepository conversationMessageRepository,
            ConversationsService conversationsService
    ) {
        this.chatService = chatService;
        this.templateService = templateService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.conversationRepository = conversationRepository;
        this.conversationMessageRepository = conversationMessageRepository;
        this.conversationsService = conversationsService;
    }

    @MessageMapping("/send-message")
    public void sendMessage(
            ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        ConversationMessage cm = new ConversationMessage();

        Conversation conversation = this.conversationsService.getConversationWithUserOrCreateNew(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId()
        );

        cm.setMessage(chatMessage.getText());
        cm.setConversation(conversation);
        cm.setCreatedAt(LocalDateTime.now());
        cm.setUpdatedAt(LocalDateTime.now());

        conversationMessageRepository.save(cm);

        doSendMessages(headerAccessor, conversation.getId());
    }

    @MessageMapping("/load-message")
    public void triggerMessageGet(
            TriggerDto triggerDto,
            SimpMessageHeaderAccessor headerAccessor
    ) {
       doSendMessages(headerAccessor, triggerDto.getConversationId());
    }

    private void doSendMessages(SimpMessageHeaderAccessor headerAccessor, Long cId) {
        String username = headerAccessor.getUser().getName();
        System.out.println("conversation id " + cId);
        List<ConversationMessage> messages =  chatService.getMessageFromConversation(cId);
        Map<String, Object> model = new HashMap<>();
        System.out.println(messages.size());
        model.put("messages", messages);

        String html = "";
        try {
            html = templateService.render("user/chat/components/message.ftl", model);
        } catch (Exception e) {
            System.out.println("Exception error render " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("template", html);

        String payload = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            payload = mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        simpMessagingTemplate.convertAndSendToUser(
                username,
                "/queue/message",
                payload,
                headerAccessor.getMessageHeaders()
        );
    }
}
