package com.naumoff.rnc.unit.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.services.chat.MessageService;
import com.naumoff.rnc.services.chat.SaveMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private ConversationMessageRepository conversationMessageRepository;

    @Mock
    private SaveMessageService saveMessageService;

    @InjectMocks
    private MessageService messageService;

    private Conversation conversation;
    private UserEntity currentUser;
    private ConversationMessage message;

    @BeforeEach
    void setUp() {
        currentUser = new UserEntity();
        currentUser.setId(1L);

        conversation = new Conversation();
        conversation.setId(100L);

        message = new ConversationMessage();
        message.setId(500L);
        message.setMessage("Привет!");
        message.setIsFavorite(false);
    }

    // ── getConversationMessages ───────────────────────────────

    @Test
    @DisplayName("getConversationMessages — возвращает сообщения по ASC из репозитория")
    void getConversationMessages_returnsMessages() {
        // Arrange
        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(100L))
                .thenReturn(List.of(message));

        // Act
        List<ConversationMessage> result = messageService.getConversationMessages(conversation);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Привет!");
        verify(conversationMessageRepository, times(1))
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(100L);
    }

    @Test
    @DisplayName("getConversationMessages — пустой список, если сообщений нет")
    void getConversationMessages_emptyList() {
        // Arrange
        when(conversationMessageRepository
                .findByConversationIdAndDeletedAtIsNullOrderByCreatedAtAsc(100L))
                .thenReturn(List.of());

        // Act
        List<ConversationMessage> result = messageService.getConversationMessages(conversation);

        // Assert
        assertThat(result).isEmpty();
    }

    // ── getConversationMessagesIsFavorite ───────────────────

    @Test
    @DisplayName("getConversationMessagesIsFavorite — возвращает избранные сообщения")
    void getConversationMessagesIsFavorite_returnsFavoriteMessages() {
        // Arrange
        ConversationMessage favoriteMessage = new ConversationMessage();
        favoriteMessage.setId(600L);
        favoriteMessage.setMessage("Важное сообщение");
        favoriteMessage.setIsFavorite(true);

        when(conversationMessageRepository
                .findByConversationIdAndIsFavoriteTrueOrderByFavoritedAtDesc(100L))
                .thenReturn(List.of(favoriteMessage));

        // Act
        List<ConversationMessage> result = messageService.getConversationMessagesIsFavorite(conversation);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIsFavorite()).isTrue();
        verify(conversationMessageRepository, times(1))
                .findByConversationIdAndIsFavoriteTrueOrderByFavoritedAtDesc(100L);
    }

    // ── getMessageById ───────────────────────────────────────

    @Test
    @DisplayName("getMessageById — возвращает сообщение, если найдено")
    void getMessageById_found() {
        // Arrange
        when(conversationMessageRepository.findById(500L)).thenReturn(Optional.of(message));

        // Act
        ConversationMessage result = messageService.getMessageById(500L);

        // Assert
        assertThat(result).isSameAs(message);
    }

    @Test
    @DisplayName("getMessageById — бросает NoSuchElementException, если не найдено")
    void getMessageById_notFound_throws() {
        // Arrange
        when(conversationMessageRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> messageService.getMessageById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    // ── markMessageIsFavorite ───────────────────────────────

    @Test
    @DisplayName("markMessageIsFavorite — устанавливает isFavorite и favoritedAt, сохраняет")
    void markMessageIsFavorite_setsFavoriteAndSaves() {
        // Arrange
        when(conversationMessageRepository.save(any(ConversationMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ConversationMessage result = messageService.markMessageIsFavorite(message);

        // Assert
        ArgumentCaptor<ConversationMessage> captor = ArgumentCaptor.forClass(ConversationMessage.class);
        verify(conversationMessageRepository).save(captor.capture());

        ConversationMessage saved = captor.getValue();
        assertThat(saved.getIsFavorite()).isTrue();
        assertThat(saved.getFavoritedAt()).isNotNull();
        assertThat(result).isSameAs(saved);
    }

    // ── sendMessageMySelf ───────────────────────────────────

    @Test
    @DisplayName("sendMessageMySelf — делегирует в saveMessageService с корректным ChatMessage")
    void sendMessageMySelf_delegatesToSaveMessageService() {
        // Arrange
        doNothing().when(saveMessageService).saveMessage(eq(1L), eq(conversation), any(ChatMessage.class));

        // Act
        messageService.sendMessageMySelf(conversation, currentUser);

        // Assert
        ArgumentCaptor<ChatMessage> chatMessageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
        verify(saveMessageService, times(1)).saveMessage(eq(1L), eq(conversation), chatMessageCaptor.capture());

        ChatMessage savedChatMessage = chatMessageCaptor.getValue();
        assertThat(savedChatMessage.getConversationId()).isEqualTo(100L);
        assertThat(savedChatMessage.getSenderId()).isEqualTo(1L);
        assertThat(savedChatMessage.getRecipientId()).isEqualTo(1L);
        assertThat(savedChatMessage.getText()).contains("сообщения сами себе");
    }

    // ── getCountUnreadMessages ──────────────────────────────

    @Test
    @DisplayName("getCountUnreadMessages — возвращает количество непрочитанных сообщений")
    void getCountUnreadMessages_returnsCount() {
        // Arrange
        when(conversationMessageRepository.countAllUnreadMessages(currentUser))
                .thenReturn(7L);

        // Act
        Long result = messageService.getCountUnreadMessages(currentUser);

        // Assert
        assertThat(result).isEqualTo(7L);
        verify(conversationMessageRepository, times(1)).countAllUnreadMessages(currentUser);
    }

    @Test
    @DisplayName("getCountUnreadMessages — возвращает 0, если непрочитанных нет")
    void getCountUnreadMessages_zero() {
        // Arrange
        when(conversationMessageRepository.countAllUnreadMessages(currentUser))
                .thenReturn(0L);

        // Act
        Long result = messageService.getCountUnreadMessages(currentUser);

        // Assert
        assertThat(result).isZero();
    }
}
