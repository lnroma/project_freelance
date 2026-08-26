package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaveMessageService {

    private final ConversationsService conversationsService;
    private final ConversationMessageRepository conversationMessageRepository;

    public SaveMessageService(
            ConversationsService conversationsService,
            ConversationMessageRepository conversationMessageRepository
    ) {
        this.conversationsService = conversationsService;
        this.conversationMessageRepository = conversationMessageRepository;
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
        cm.setSenderId(currentUserId);
        cm.setIsRead(false);

        conversationsService.setOrderWidthToConversation(conversation, 2L);

        this.conversationMessageRepository.save(cm);

        return cm;
    }
}
