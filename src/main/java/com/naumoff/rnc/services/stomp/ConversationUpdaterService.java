package com.naumoff.rnc.services.stomp;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.services.TemplateService;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.users.UserService;
import freemarker.template.TemplateException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConversationUpdaterService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final TemplateService templateService;
    private final StompService stompService;

    public ConversationUpdaterService(
            ConversationsService conversationsService,
            SimpMessagingTemplate simpMessagingTemplate,
            ChatService chatService,
            TemplateService templateService,
            StompService stompService
    ) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
        this.templateService = templateService;
        this.stompService = stompService;
    }

    /**
     * this main method for updated conversations list of users
     *
     * @param userFrom first user in conversation
     * @param userTo second user in conversation
     * @param messageHeaders message headers from stomp
     * @param currentConversation current conversation
     */
    public void updateConversationList(
            UserEntity userFrom,
            UserEntity userTo,
            MessageHeaders messageHeaders,
            Conversation currentConversation
    ) {
        try {
            sendToUser(userFrom, currentConversation, messageHeaders);
            sendToUser(userTo, currentConversation, messageHeaders);
        } catch (Exception exception) {
            System.out.println("Error in generated template");
            exception.printStackTrace();
        }
    }

    /**
     * Send to socket for user generated data of conversation list
     *
     * @param currentUser just user entity
     * @param currentConversation current conversation entity
     * @param messageHeaders headers of messages
     * @throws TemplateException template error
     * @throws IOException error in filesystem
     */
    private void sendToUser(
            UserEntity currentUser,
            Conversation currentConversation,
            MessageHeaders messageHeaders
    ) throws TemplateException, IOException {
        String html = generateHtmlForUser(currentUser, currentConversation);
        String payload = generatePayload(html);
        sendResultToSocket(currentUser, payload, messageHeaders);
    }

    /**
     * Generate html response for each users
     *
     * @param currentUser just user entity for generate
     * @param currentConversation current conversation entity
     * @return html string for payload
     * @throws TemplateException error in template
     * @throws IOException error in file system
     */
    private String generateHtmlForUser(
            UserEntity currentUser,
            Conversation currentConversation
    ) throws TemplateException, IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("conversations", chatService.getLastChats(currentUser));
        model.put("currentConversation", currentConversation);

        return templateService.render("user/chat/components/conversation.ftl", model);
    }

    /**
     * Generate payload for each users
     *
     * @param resultHtml is rendered html for each user
     * @return string
     */
    private String generatePayload(String resultHtml) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("conversations", resultHtml);

        return stompService.generateJson(payload);
    }

    /**
     * Send result after all generated to socket
     * @param user user for him send
     * @param jsonString payload
     * @param messageHeaders message headers
     */
    private void sendResultToSocket(
            UserEntity user,
            String jsonString,
            MessageHeaders messageHeaders
    ) {
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(user.getId()),
                "/queue/conversation",
                jsonString,
                messageHeaders
        );
    }
}
