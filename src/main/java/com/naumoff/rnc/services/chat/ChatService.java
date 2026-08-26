package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.dto.chat.ChatDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ChatService {

    final private ConversationMessageRepository conversationMessageRepository;
    final private ConversationRepository conversationRepository;
    final private UserRepository userRepository;

    public ChatService(
            ConversationRepository conversationRepository,
            ConversationMessageRepository conversationMessageRepository,
            UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationMessageRepository = conversationMessageRepository;
        this.userRepository = userRepository;
    }

    public List<ChatDto> getLastChats(UserEntity currentUser) {
        List<ChatDto> chats = new ArrayList<>();

        List<Conversation> conversations = this.conversationRepository.findByUserFromOrUserToAndDeletedAtIsNullOrderByCreatedAtDesc(
                currentUser.getId(),
                currentUser.getId()
        );

        if (conversations.isEmpty()) {
            return null;
        }

        conversations.forEach(conversation -> {
            Long recipientId = conversation.getUserFrom();
            if (recipientId.equals(currentUser.getId())) {
                recipientId = conversation.getUserTo();
            }

            UserEntity recipientUser;
            try {
                recipientUser = this.userRepository.findById(recipientId).get();
            } catch (Exception e) {
                recipientUser = currentUser;
            }

            List<ConversationMessage> conversationMessages = this.conversationMessageRepository
                    .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(conversation.getId());

            String lastMessage = "Сообщений пока нет";
            if (!conversationMessages.isEmpty()) {
                ConversationMessage convMessage = conversationMessages.get(0);
                lastMessage = convMessage.getMessage();
            }

            chats.add(new ChatDto(recipientUser.getEmail(), lastMessage, conversation.getId(), recipientId));
        });

        if (!chats.isEmpty()) {
            chats.get(0).setActive(true);
        }

        return chats;
    }

    public ChatDto getActiveChat(List<ChatDto> chats) {
        AtomicReference<ChatDto> chatDtoResult = new AtomicReference<>();
        if (!chats.isEmpty()) {
            chats.forEach(chatDto -> {
                if (chatDto.getActive()) {
                    chatDtoResult.set(chatDto);
                }
            });
        }

        return chatDtoResult.get();
    }

    public List<ConversationMessage> getMessageFromConversation(Long conversationId) {
        return conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(
                        conversationId
                );
    }

    public List<ConversationMessage> getLastMessageFromConversation(Long conversationId) {
        return doGet(conversationId);
    }

    public List<ConversationMessage> getLastMessageFromConversation(List<ChatDto> chatDtos) {
        ChatDto currentActiveChat = getActiveChat(chatDtos);

        return doGet(currentActiveChat.getConversationId());
    }

    public List<ConversationMessage> doGet(Long conversationId) {
        return this.conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(conversationId);
    }
}
