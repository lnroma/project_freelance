package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.services.users.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaveMessageService {

    private final ConversationsService conversationsService;
    private final ConversationMessageRepository conversationMessageRepository;
    private final UserService userService;

    public SaveMessageService(
            ConversationsService conversationsService,
            ConversationMessageRepository conversationMessageRepository,
            UserService userService) {
        this.conversationsService = conversationsService;
        this.conversationMessageRepository = conversationMessageRepository;
        this.userService = userService;
    }

    public ConversationMessage saveMessage(
            Long currentUserId,
            Conversation conversation,
            ChatMessage chatMessage
    ) {
        ConversationMessage cm = new ConversationMessage();
        cm.setMessage(chatMessage.getText());
        cm.setConversation(conversation);
        cm.setCreatedAt(LocalDateTime.now());
        cm.setUpdatedAt(LocalDateTime.now());
        cm.setSender(userService.getUserById(currentUserId));
        cm.setIsRead(false);

        conversationsService.upConversation(conversation);

        this.conversationMessageRepository.save(cm);

        return cm;
    }
}
