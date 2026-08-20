package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    final private ConversationMessageRepository conversationMessageRepository;

    public MessageService(
            ConversationMessageRepository conversationMessageRepository
    ) {
        this.conversationMessageRepository = conversationMessageRepository;
    }

    public List<ConversationMessage> getConversationMessages(Conversation conversation) {
        return this.conversationMessageRepository.findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(conversation.getId());
    }

}
