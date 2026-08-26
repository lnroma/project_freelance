package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.services.users.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationsService {

    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final ConversationMessageRepository conversationMessageRepository;

    public ConversationsService(
            ConversationRepository conversationRepository,
            UserService userService,
            ConversationMessageRepository conversationMessageRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.userService = userService;
        this.conversationMessageRepository = conversationMessageRepository;
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
     * Load conversation by id
     *
     * @param cId Long identificator of conversation
     * @return conversation entity
     */
    public Conversation getConversationById(Long cId) {
        return conversationRepository.findById(cId).get();
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

        if (userFrom.equals(userTo)) {
            conversation.setOrderWidth(999L);
        }

        conversation.setOrderWidth(2L);

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

    public Conversation createConversationForSelfMessages(UserEntity currentUser) {
        Conversation conversation = getConversationForRecipient(currentUser.getId(), currentUser.getId());
        if (conversation != null) {
            return conversation;
        }

        return createConversation(currentUser.getId(), currentUser.getId());
    }

    public UserEntity getRecipient(UserEntity currentUser, Conversation currentConversation) {
        if (currentUser.getId().equals(currentConversation.getUserFrom())) {
            return userService.getUserById(currentConversation.getUserTo());
        } else {
            return userService.getUserById(currentConversation.getUserFrom());
        }
    }

    public Conversation setOrderWidthToConversation(Conversation conversation, Long orderWidth) {
        conversation.setOrderWidth(orderWidth);
        conversationRepository.save(conversation);

        return conversation;
    }

    public Conversation setIsReadAllMessagesInConversation(Conversation conversation) {
        conversationMessageRepository.setIsReadMessagesByConversationId(conversation.getId());

        return conversation;
    }
}
