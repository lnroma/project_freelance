package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    final private ConversationMessageRepository conversationMessageRepository;
    private final SaveMessageService saveMessageService;

    public MessageService(
            ConversationMessageRepository conversationMessageRepository,
            SaveMessageService saveMessageService
    ) {
        this.conversationMessageRepository = conversationMessageRepository;
        this.saveMessageService = saveMessageService;
    }

    public List<ConversationMessage> getConversationMessages(Conversation conversation) {
        return this.conversationMessageRepository.findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(
                conversation.getId()
        );
    }

    public void sendMessageMySelf(Conversation conversation, UserEntity currentUser) {
        ChatMessage chatMessage = ChatMessage.builder()
                .conversationId(conversation.getId())
                .senderId(currentUser.getId())
                .recipientId(currentUser.getId())
                .text("В данный чат вы можете отправлять сообщения сами себе что бы сохранить их как заметки")
                .build();

        saveMessageService.saveMessage(
                currentUser.getId(),
                conversation,
                chatMessage
        );
    }

    public Long getCountUnreadMessages(UserEntity currentUser) {
        return conversationMessageRepository.countAllUnreadMessages(currentUser);
    }
}
