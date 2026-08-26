package com.naumoff.rnc.database.repository.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
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
    @Query("UPDATE ConversationMessage cm SET cm.isRead = true WHERE cm.conversation = :convId")
    void setIsReadMessagesByConversationId(@Param("convId") Long convId);
}
