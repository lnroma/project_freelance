package com.naumoff.rnc.database.repository.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUserFromAndDeletedAtIsNull(Long userId);
    List<Conversation> findByUserToAndDeletedAtIsNull(Long userId);
    List<Conversation> findByUserFromOrUserToAndDeletedAtIsNullOrderByCreatedAtDesc(
            @Param("userFrom") Long userId,
            @Param("userTo") Long userIdSame
    );

    @Query("SELECT c FROM Conversation c " +
            "WHERE ((c.userFrom = :fromId AND c.userTo = :toId) " +
            "   OR (c.userFrom = :toId AND c.userTo = :fromId)) " +
            "  AND c.deletedAt IS NULL")
    Optional<Conversation> findConversationBetweenUsers(
            @Param("fromId") Long fromId,
            @Param("toId") Long toId
    );
}
