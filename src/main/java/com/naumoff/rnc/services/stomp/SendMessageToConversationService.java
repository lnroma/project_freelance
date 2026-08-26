package com.naumoff.rnc.services.stomp;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.services.TemplateService;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.chat.MessageService;
import com.naumoff.rnc.services.users.UserService;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendMessageToConversationService {

    private final ConversationsService conversationsService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final TemplateService templateService;
    private final StompService stompService;

    public SendMessageToConversationService(
            ConversationsService conversationsService,
            UserService userService,
            SimpMessagingTemplate simpMessagingTemplate,
            MessageService messageService,
            TemplateService templateService,
            StompService stompService
    ) {
        this.conversationsService = conversationsService;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
        this.templateService = templateService;
        this.stompService = stompService;
    }


    public void sendMessages(
            SimpMessageHeaderAccessor headerAccessor,
            Long cId
    ) {
        Conversation currentConversation = conversationsService.getConversationById(cId);
        String currentUserName = headerAccessor.getUser().getName();

        UserEntity currentUser = userService.getUserById(Long.valueOf(currentUserName));
        UserEntity recipientUser = conversationsService.getRecipient(currentUser, currentConversation);

        sendMessageToSocket(currentUser, recipientUser, currentConversation, headerAccessor);
        sendMessageToSocket(recipientUser, currentUser, currentConversation, headerAccessor);
    }

    private void sendMessageToSocket(
            UserEntity currentUser,
            UserEntity recipientUser,
            Conversation currentConversation,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // send message to first user in coversation
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(currentUser.getId()),
                "/queue/message",
                generatePayloadForUsers(currentUser, recipientUser, currentConversation),
                headerAccessor.getMessageHeaders()
        );
    }

    private String generatePayloadForUsers(
            UserEntity currentUser,
            UserEntity recipientUser,
            Conversation conversation
    ) {
        List<ConversationMessage> messages =  messageService.getConversationMessages(conversation);
        Map<String, Object> model = new HashMap<>();
        model.put("messages", messages);
        model.put("currentUser", currentUser);
        model.put("userTo", recipientUser);

        String html = "";
        try {
            html = templateService.render("user/chat/components/message.ftl", model);
        } catch (Exception e) {
            System.out.println("Exception error render " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("template", html);
        result.put("conversationId", conversation.getId());

        return stompService.generateJson(result);
    }
}
