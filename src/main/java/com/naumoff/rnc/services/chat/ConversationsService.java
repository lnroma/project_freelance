package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationsService {

    private final ConversationRepository conversationRepository;

    public ConversationsService(
            ConversationRepository conversationRepository
    ) {
        this.conversationRepository = conversationRepository;
    }

    public List<Conversation> getAllConversations(UserEntity user) {
        return this.conversationRepository.findByUserFromOrUserToAndDeletedAtIsNullOrderByCreatedAtDesc(
                user.getId(),
                user.getId()
        );
    }

    /**
     * get conversation between users
     *
     * @param userFrom user from
     * @param userTo user to
     * @return conversation instance
     */
    public Conversation getConversationForRecipient(Long userFrom, Long userTo) {
        return this.conversationRepository.findConversationBetweenUsers(userFrom, userTo).orElse(null);
    }

    /**
     * create conversation for users
     *
     * @param userFrom user from
     * @param userTo user to
     * @return conversation instance
     */
    public Conversation createConversation(Long userFrom, Long userTo) {
        Conversation conversation = new Conversation();

        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setUserFrom(userFrom);
        conversation.setUserTo(userTo);

        conversation = this.conversationRepository.save(conversation);

        return conversation;
    }

    /**
     * Get conversations with users or create
     *
     * @param userFrom user sender
     * @param userTo user recipient
     * @return conversation
     */
    public Conversation getConversationWithUserOrCreateNew(Long userFrom, Long userTo) {
        Conversation conversation = getConversationForRecipient(userFrom, userTo);

        if (conversation == null) {
            conversation = createConversation(userFrom, userTo);
        }

        return conversation;
    }
}
