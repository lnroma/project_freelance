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

        List<Conversation> conversations = this.conversationRepository.findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                currentUser,
                currentUser
        );

        if (conversations.isEmpty()) {
            return null;
        }

        conversations.forEach(conversation -> {
            UserEntity recipientUser = conversation.getUserFromEntity();

            if (recipientUser.equals(currentUser)) {
                recipientUser = conversation.getUserToEntity();
            }

            List<ConversationMessage> conversationMessages = this.conversationMessageRepository
                    .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(conversation.getId());

            String lastMessage = "Сообщений пока нет";
            if (!conversationMessages.isEmpty()) {
                ConversationMessage convMessage = conversationMessages.get(0);
                lastMessage = convMessage.getMessage();
            }

            ChatDto chatDto = ChatDto.builder()
                    .isActive(false)
                    .recipientName(recipientUser.getEmail())
                    .lastMessage(lastMessage)
                    .conversationId(conversation.getId())
                    .recipientId(recipientUser.getId())
                    .countUnreadMessages(conversationMessageRepository.countUnreadMessages(
                            conversation,
                            currentUser
                    ))
                    .build();

            chats.add(chatDto);
        });

        if (!chats.isEmpty()) {
            chats.get(0).setIsActive(true);
        }

        return chats;
    }

    public ChatDto getActiveChat(List<ChatDto> chats) {
        AtomicReference<ChatDto> chatDtoResult = new AtomicReference<>();
        if (!chats.isEmpty()) {
            chats.forEach(chatDto -> {
                if (chatDto.getIsActive()) {
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
