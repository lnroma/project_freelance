package com.naumoff.rnc.database.repository.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long> {

    List<ConversationMessage> findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(
            @Param("conversationId")
            Long conversationId
    );

    List<ConversationMessage> findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(
            @Param("conversationId")
            Long conversationId
    );

    Long conversation(Conversation conversation);

    @Modifying
    @Transactional
    @Query("UPDATE ConversationMessage cm SET cm.isRead = true WHERE cm.conversation = :conversation and cm.sender != :currentUser")
    void setIsReadMessagesByConversationId(
            @Param("conversation") Conversation conversation,
            @Param("currentUser") UserEntity currentUser
            );

    @Query("SELECT COUNT(cm.id) FROM ConversationMessage cm WHERE" +
            " cm.isRead = false " +
            " and cm.conversation = :conversation" +
            " and cm.sender != :currentUser")
    Long countUnreadMessages(
            @Param("conversation") Conversation conversation,
            @Param("currentUser") UserEntity currentUser
    );

    @Query("SELECT COUNT(cm.id) FROM ConversationMessage cm " +
            "LEFT JOIN Conversation c ON c = cm.conversation WHERE " +
            " (c.userFromEntity = :currentUser OR c.userToEntity = :currentUser)" +
            " AND cm.sender != :currentUser")
    Long countAllUnreadMessages(
            @Param("currentUser") UserEntity currentUser
    );
}
