package com.naumoff.rnc.services.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.services.users.UserService;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationsService {

    private final ConversationRepository conversationRepository;
    private final ConversationMessageRepository conversationMessageRepository;

    public ConversationsService(
            ConversationRepository conversationRepository,
            ConversationMessageRepository conversationMessageRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationMessageRepository = conversationMessageRepository;
    }

    public List<Conversation> getAllConversations(UserEntity user) {
        return this.conversationRepository.findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                user,
                user
        );
    }

    /**
     * get conversation between users
     *
     * @param userFrom user from
     * @param userTo user to
     * @return conversation instance
     */
    public Conversation getConversationForRecipient(UserEntity userFrom, UserEntity userTo) {
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
    public Conversation createConversation(UserEntity userFrom, UserEntity userTo) {
        Conversation oldConv = getConversationForRecipient(userFrom, userTo);
        if (oldConv != null) {
            return oldConv;
        }

        Conversation conversation = new Conversation();

        conversation.setCreatedAt(LocalDateTime.now());
        conversation.setUserFromEntity(userFrom);
        conversation.setUserToEntity(userTo);

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
    public Conversation getConversationWithUserOrCreateNew(UserEntity userFrom, UserEntity userTo) {
        Conversation conversation = getConversationForRecipient(userFrom, userTo);

        if (conversation == null) {
            conversation = createConversation(userFrom, userTo);
        }

        return conversation;
    }

    public Conversation createConversationForSelfMessages(UserEntity currentUser) {
        Conversation conversation = getConversationForRecipient(currentUser, currentUser);
        if (conversation != null) {
            return conversation;
        }

        return createConversation(currentUser, currentUser);
    }

    public UserEntity getRecipient(UserEntity currentUser, Conversation currentConversation) {
        if (currentUser.getId().equals(currentConversation.getUserFromEntity().getId())) {
            return currentConversation.getUserToEntity();
        } else {
            return currentConversation.getUserFromEntity();
        }
    }

    public Conversation setOrderWidthToConversation(Conversation conversation, Long orderWidth) {
        conversation.setOrderWidth(orderWidth);
        conversationRepository.save(conversation);

        return conversation;
    }

    public Conversation setIsReadAllMessagesInConversation(
            Conversation conversation,
            UserEntity currentUser
    ) {
        conversationMessageRepository.setIsReadMessagesByConversationId(conversation, currentUser);

        return conversation;
    }

    public Conversation upConversation(Conversation conversation) {
        Long currentWidth = conversation.getOrderWidth();
        currentWidth++;
        conversation.setOrderWidth(currentWidth);

        conversationRepository.save(conversation);

        return conversation;
    }

    public Conversation downConversation(Conversation conversation) {
        Long currentWidth = conversation.getOrderWidth();

        if (currentWidth.equals(0L)) {
            return conversation;
        }

        if (currentWidth < 0L) {
            conversation.setOrderWidth(0L);
        }

        currentWidth--;
        conversation.setOrderWidth(currentWidth);

        return conversation;
    }

    /**
     * Check conversation ownered by current user
     *
     * @param currentUser Current user entity
     * @param currentConversation current conversation entity
     * @return boolean true if conversation ownered by user
     */
    public boolean checkConversationIsUser(
            UserEntity currentUser,
            Conversation currentConversation
    ) {
        if (currentConversation.getUserToEntity().getId().equals(currentUser.getId())) {
            return true;
        }

        if (currentConversation.getUserFromEntity().getId().equals(currentUser.getId())) {
            return true;
        }

        return false;
    }

    public Conversation resetConversationWidth(Conversation conversation) {
        conversation.setOrderWidth(0L);

        return conversation;
    }
}
