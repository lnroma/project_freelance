package com.naumoff.rnc.database.repository.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByUserFromEntityAndDeletedAtIsNull(UserEntity userEntity);
    List<Conversation> findByUserToEntityAndDeletedAtIsNull(UserEntity userEntity);
    List<Conversation> findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByCreatedAtDesc(
            @Param("userFrom") UserEntity userFrom,
            @Param("userTo") UserEntity userSame
    );

    List<Conversation> findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthAsc(
            @Param("userFrom") UserEntity userFrom,
            @Param("userTo") UserEntity userSame
    );


    List<Conversation> findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
            @Param("userFrom") UserEntity currentUser,
            @Param("userTo") UserEntity userSame
    );

    @Query("SELECT c FROM Conversation c " +
            "WHERE ((c.userFromEntity = :fromId AND c.userToEntity = :toId) " +
            "   OR (c.userFromEntity = :toId AND c.userToEntity = :fromId)) " +
            "  AND c.deletedAt IS NULL")
    Optional<Conversation> findConversationBetweenUsers(
            @Param("fromId") UserEntity userFrom,
            @Param("toId") UserEntity userTo
    );
}
