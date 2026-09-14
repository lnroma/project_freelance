package com.naumoff.rnc.unit.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.dto.chat.ChatDto;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.users.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private ConversationMessageRepository conversationMessageRepository;

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private ChatService chatService;

    private UserEntity currentUser;
    private UserEntity recipientUser;
    private Conversation conversation;
    private ConversationMessage message;
    private UserProfileEntity recipientProfile;

    @BeforeEach
    void setUp() {
        currentUser = new UserEntity();
        currentUser.setId(1L);

        recipientUser = new UserEntity();
        recipientUser.setId(2L);

        conversation = new Conversation();
        conversation.setId(100L);
        conversation.setUserFromEntity(currentUser);
        conversation.setUserToEntity(recipientUser);

        message = new ConversationMessage();
        message.setId(500L);
        message.setMessage("Привет!");
        message.setCreatedAt(LocalDateTime.now());

        recipientProfile = new UserProfileEntity();
        recipientProfile.setFirstName("Иван");
        recipientProfile.setLastName("Иванов");
    }

    // ── getLastChats ─────────────────────────────────────────

    @Test
    @DisplayName("getLastChats — возвращает null, если нет диалогов")
    void getLastChats_noConversations_returnsNull() {
        // Arrange
        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of());

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("getLastChats — корректно строит список чатов с сообщениями и профилем")
    void getLastChats_withConversationsAndMessages() {
        // Arrange
        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of(conversation));

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of(message));

        when(userProfileService.getCurrentUserProfile(recipientUser))
                .thenReturn(recipientProfile);

        when(conversationMessageRepository.countUnreadMessages(conversation, currentUser))
                .thenReturn(3L);

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result).hasSize(1);

        ChatDto chatDto = result.get(0);
        assertThat(chatDto.getConversationId()).isEqualTo(100L);
        assertThat(chatDto.getRecipientId()).isEqualTo(2L);
        assertThat(chatDto.getRecipientName()).isEqualTo("Иван Иванов");
        assertThat(chatDto.getLastMessage()).isEqualTo("Привет!");
        assertThat(chatDto.getCountUnreadMessages()).isEqualTo(3L);
        assertThat(chatDto.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("getLastChats — подставляет 'Сообщений пока нет', если нет сообщений")
    void getLastChats_noMessages() {
        // Arrange
        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of(conversation));

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of());

        when(userProfileService.getCurrentUserProfile(recipientUser))
                .thenReturn(recipientProfile);

        when(conversationMessageRepository.countUnreadMessages(conversation, currentUser))
                .thenReturn(0L);

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result.get(0).getLastMessage()).isEqualTo("Сообщений пока нет");
    }

    @Test
    @DisplayName("getLastChats — подставляет 'Пользователь с id: ...', если профиль не найден")
    void getLastChats_noProfile() {
        // Arrange
        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of(conversation));

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of(message));

        when(userProfileService.getCurrentUserProfile(recipientUser))
                .thenReturn(null);

        when(conversationMessageRepository.countUnreadMessages(conversation, currentUser))
                .thenReturn(0L);

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result.get(0).getRecipientName()).isEqualTo("Пользователь с id: 2");
    }

    @Test
    @DisplayName("getLastChats — определяет получателя как userTo, если currentUser = userFrom")
    void getLastChats_recipientIsUserToWhenCurrentUserIsUserFrom() {
        // Arrange
        conversation.setUserFromEntity(currentUser);
        conversation.setUserToEntity(recipientUser);

        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of(conversation));

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of());

        when(userProfileService.getCurrentUserProfile(recipientUser))
                .thenReturn(recipientProfile);

        when(conversationMessageRepository.countUnreadMessages(conversation, currentUser))
                .thenReturn(0L);

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result.get(0).getRecipientId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("getLastChats — определяет получателя как userFrom, если currentUser = userTo")
    void getLastChats_recipientIsUserFromWhenCurrentUserIsUserTo() {
        // Arrange — меняем местами: currentUser получатель, recipientUser отправитель
        conversation.setUserFromEntity(recipientUser);
        conversation.setUserToEntity(currentUser);

        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of(conversation));

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of());

        when(userProfileService.getCurrentUserProfile(recipientUser))
                .thenReturn(recipientProfile);

        when(conversationMessageRepository.countUnreadMessages(conversation, currentUser))
                .thenReturn(0L);

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result.get(0).getRecipientId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("getLastChats — только первый чат в списке активен")
    void getLastChats_onlyFirstChatIsActive() {
        // Arrange
        Conversation conv1 = new Conversation();
        conv1.setId(100L);
        conv1.setUserFromEntity(currentUser);
        conv1.setUserToEntity(recipientUser);

        UserEntity recipient2 = new UserEntity();
        recipient2.setId(3L);

        Conversation conv2 = new Conversation();
        conv2.setId(200L);
        conv2.setUserFromEntity(currentUser);
        conv2.setUserToEntity(recipient2);

        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        currentUser, currentUser))
                .thenReturn(List.of(conv1, conv2));

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(any(Long.class)))
                .thenReturn(List.of());

        when(userProfileService.getCurrentUserProfile(any(UserEntity.class)))
                .thenReturn(null);

        when(conversationMessageRepository.countUnreadMessages(any(Conversation.class), eq(currentUser)))
                .thenReturn(0L);

        // Act
        List<ChatDto> result = chatService.getLastChats(currentUser);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIsActive()).isTrue();
        assertThat(result.get(1).getIsActive()).isFalse();
    }

    // ── getActiveChat ────────────────────────────────────────

    @Test
    @DisplayName("getActiveChat — возвращает активный чат из списка")
    void getActiveChat_returnsActiveChat() {
        // Arrange
        ChatDto active = ChatDto.builder().conversationId(100L).isActive(true).build();
        ChatDto inactive = ChatDto.builder().conversationId(200L).isActive(false).build();

        // Act
        ChatDto result = chatService.getActiveChat(List.of(inactive, active));

        // Assert
        assertThat(result).isSameAs(active);
    }

    @Test
    @DisplayName("getActiveChat — возвращает null, если нет активного чата")
    void getActiveChat_noActiveChat_returnsNull() {
        // Arrange
        ChatDto chat1 = ChatDto.builder().conversationId(100L).isActive(false).build();
        ChatDto chat2 = ChatDto.builder().conversationId(200L).isActive(false).build();

        // Act
        ChatDto result = chatService.getActiveChat(List.of(chat1, chat2));

        // Assert
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("getActiveChat — возвращает null для пустого списка")
    void getActiveChat_emptyList_returnsNull() {
        // Act
        ChatDto result = chatService.getActiveChat(List.of());

        // Assert
        assertThat(result).isNull();
    }

    // ── getMessageFromConversation ───────────────────────────

    @Test
    @DisplayName("getMessageFromConversation — возвращает сообщения по ASC")
    void getMessageFromConversation_returnsMessagesAsc() {
        // Arrange
        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(100L))
                .thenReturn(List.of(message));

        // Act
        List<ConversationMessage> result = chatService.getMessageFromConversation(100L);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Привет!");
        verify(conversationMessageRepository, times(1))
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(100L);
    }

    // ── getLastMessageFromConversation (by id) ───────────────

    @Test
    @DisplayName("getLastMessageFromConversation (Long) — возвращает сообщения по DESC")
    void getLastMessageFromConversation_byId_returnsMessagesDesc() {
        // Arrange
        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of(message));

        // Act
        List<ConversationMessage> result = chatService.getLastMessageFromConversation(100L);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Привет!");
        verify(conversationMessageRepository, times(1))
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L);
    }

    // ── getLastMessageFromConversation (by chatDtos) ─────────

    @Test
    @DisplayName("getLastMessageFromConversation (List<ChatDto>) — берёт ID активного чата и возвращает сообщения")
    void getLastMessageFromConversation_byChatDtos() {
        // Arrange
        ChatDto activeChat = ChatDto.builder().conversationId(100L).isActive(true).build();
        ChatDto inactiveChat = ChatDto.builder().conversationId(200L).isActive(false).build();

        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of(message));

        // Act
        List<ConversationMessage> result = chatService.getLastMessageFromConversation(
                List.of(inactiveChat, activeChat));

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Привет!");
        verify(conversationMessageRepository, times(1))
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L);
    }

    // ── doGet ────────────────────────────────────────────────

    @Test
    @DisplayName("doGet — делегирует вызов в репозиторий с DESC сортировкой")
    void doGet_delegatesToRepository() {
        // Arrange
        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L))
                .thenReturn(List.of(message));

        // Act
        List<ConversationMessage> result = chatService.doGet(100L);

        // Assert
        assertThat(result).isSameAs(List.of(message));
        verify(conversationMessageRepository, times(1))
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtDesc(100L);
    }
}
