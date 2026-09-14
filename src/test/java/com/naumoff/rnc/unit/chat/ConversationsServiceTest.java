package com.naumoff.rnc.unit.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.database.repository.chat.ConversationRepository;
import com.naumoff.rnc.services.chat.ConversationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationsServiceTest {

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private ConversationMessageRepository conversationMessageRepository;

    @InjectMocks
    private ConversationsService conversationsService;

    private UserEntity userFrom;
    private UserEntity userTo;
    private Conversation conversation;

    @BeforeEach
    void setUp() {
        userFrom = new UserEntity();
        userFrom.setId(1L);

        userTo = new UserEntity();
        userTo.setId(2L);

        conversation = new Conversation();
        conversation.setId(100L);
        conversation.setUserFromEntity(userFrom);
        conversation.setUserToEntity(userTo);
        conversation.setOrderWidth(5L);
    }

    // ── getAllConversations ──────────────────────────────────

    @Test
    @DisplayName("getAllConversations — делегирует вызов в репозиторий")
    void getAllConversations_delegatesToRepository() {
        // Arrange
        List<Conversation> expected = List.of(conversation);
        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        userFrom, userFrom))
                .thenReturn(expected);

        // Act
        List<Conversation> result = conversationsService.getAllConversations(userFrom);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("getAllConversations — возвращает пустой список, если диалогов нет")
    void getAllConversations_emptyList() {
        // Arrange
        when(conversationRepository
                .findByUserFromEntityOrUserToEntityAndDeletedAtIsNullOrderByOrderWidthDesc(
                        any(UserEntity.class), any(UserEntity.class)))
                .thenReturn(List.of());

        // Act
        List<Conversation> result = conversationsService.getAllConversations(userFrom);

        // Assert
        assertThat(result).isEmpty();
    }

    // ── getConversationForRecipient ──────────────────────────

    @Test
    @DisplayName("getConversationForRecipient — возвращает диалог, если найден")
    void getConversationForRecipient_found() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userTo))
                .thenReturn(Optional.of(conversation));

        // Act
        Conversation result = conversationsService.getConversationForRecipient(userFrom, userTo);

        // Assert
        assertThat(result).isSameAs(conversation);
    }

    @Test
    @DisplayName("getConversationForRecipient — возвращает null, если диалог не найден")
    void getConversationForRecipient_notFound() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userTo))
                .thenReturn(Optional.empty());

        // Act
        Conversation result = conversationsService.getConversationForRecipient(userFrom, userTo);

        // Assert
        assertThat(result).isNull();
    }

    // ── getConversationById ──────────────────────────────────

    @Test
    @DisplayName("getConversationById — возвращает диалог, если найден")
    void getConversationById_found() {
        // Arrange
        when(conversationRepository.findById(100L)).thenReturn(Optional.of(conversation));

        // Act
        Conversation result = conversationsService.getConversationById(100L);

        // Assert
        assertThat(result).isSameAs(conversation);
    }

    @Test
    @DisplayName("getConversationById — бросает NoSuchElementException, если не найден")
    void getConversationById_notFound_throws() {
        // Arrange
        when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> conversationsService.getConversationById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    // ── createConversation ──────────────────────────────────

    @Test
    @DisplayName("createConversation — возвращает существующий диалог, если он уже есть")
    void createConversation_alreadyExists_returnsExisting() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userTo))
                .thenReturn(Optional.of(conversation));

        // Act
        Conversation result = conversationsService.createConversation(userFrom, userTo);

        // Assert
        assertThat(result).isSameAs(conversation);
        verify(conversationRepository, never()).save(any());
    }

    @Test
    @DisplayName("createConversation — создаёт новый диалог с orderWidth = 2")
    void createConversation_newConversation() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userTo))
                .thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Conversation result = conversationsService.createConversation(userFrom, userTo);

        // Assert
        ArgumentCaptor<Conversation> captor = ArgumentCaptor.forClass(Conversation.class);
        verify(conversationRepository).save(captor.capture());

        Conversation saved = captor.getValue();
        assertThat(saved.getUserFromEntity()).isEqualTo(userFrom);
        assertThat(saved.getUserToEntity()).isEqualTo(userTo);
        assertThat(saved.getOrderWidth()).isEqualTo(2L);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(result).isSameAs(saved);
    }

    @Test
    @DisplayName("createConversation — orderWidth всегда 2, даже если userFrom == userTo (баг: 999 перезаписывается)")
    void createConversation_selfMessage_orderWidthOverwritten() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userFrom))
                .thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Conversation result = conversationsService.createConversation(userFrom, userFrom);

        // Assert
        // Метод сначала ставит 999L, затем перезаписывает на 2L
        assertThat(result.getOrderWidth()).isEqualTo(2L);
    }

    // ── getConversationWithUserOrCreateNew ──────────────────

    @Test
    @DisplayName("getConversationWithUserOrCreateNew — возвращает существующий диалог")
    void getConversationWithUserOrCreateNew_existing() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userTo))
                .thenReturn(Optional.of(conversation));

        // Act
        Conversation result = conversationsService.getConversationWithUserOrCreateNew(userFrom, userTo);

        // Assert
        assertThat(result).isSameAs(conversation);
        verify(conversationRepository, never()).save(any());
    }

    @Test
    @DisplayName("getConversationWithUserOrCreateNew — создаёт новый, если не существует")
    void getConversationWithUserOrCreateNew_createsNew() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userTo))
                .thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Conversation result = conversationsService.getConversationWithUserOrCreateNew(userFrom, userTo);

        // Assert
        assertThat(result.getUserFromEntity()).isEqualTo(userFrom);
        assertThat(result.getUserToEntity()).isEqualTo(userTo);
        verify(conversationRepository).save(any());
    }

    // ── createConversationForSelfMessages ───────────────────

    @Test
    @DisplayName("createConversationForSelfMessages — возвращает существующий диалог с собой")
    void createConversationForSelfMessages_existing() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userFrom))
                .thenReturn(Optional.of(conversation));

        // Act
        Conversation result = conversationsService.createConversationForSelfMessages(userFrom);

        // Assert
        assertThat(result).isSameAs(conversation);
        verify(conversationRepository, never()).save(any());
    }

    @Test
    @DisplayName("createConversationForSelfMessages — создаёт новый диалог с собой")
    void createConversationForSelfMessages_createsNew() {
        // Arrange
        when(conversationRepository.findConversationBetweenUsers(userFrom, userFrom))
                .thenReturn(Optional.empty());
        when(conversationRepository.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Conversation result = conversationsService.createConversationForSelfMessages(userFrom);

        // Assert
        assertThat(result.getUserFromEntity()).isEqualTo(userFrom);
        assertThat(result.getUserToEntity()).isEqualTo(userFrom);
    }

    // ── getRecipient ─────────────────────────────────────────

    @Test
    @DisplayName("getRecipient — возвращает userTo, если currentUser = userFrom")
    void getRecipient_returnsUserToWhenCurrentUserIsUserFrom() {
        // Act
        UserEntity result = conversationsService.getRecipient(userFrom, conversation);

        // Assert
        assertThat(result).isSameAs(userTo);
    }

    @Test
    @DisplayName("getRecipient — возвращает userFrom, если currentUser = userTo")
    void getRecipient_returnsUserFromWhenCurrentUserIsUserTo() {
        // Act
        UserEntity result = conversationsService.getRecipient(userTo, conversation);

        // Assert
        assertThat(result).isSameAs(userFrom);
    }

    // ── setOrderWidthToConversation ──────────────────────────

    @Test
    @DisplayName("setOrderWidthToConversation — устанавливает orderWidth и сохраняет")
    void setOrderWidthToConversation_setsAndSaves() {
        // Arrange
        when(conversationRepository.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Conversation result = conversationsService.setOrderWidthToConversation(conversation, 42L);

        // Assert
        assertThat(result.getOrderWidth()).isEqualTo(42L);
        verify(conversationRepository).save(conversation);
    }

    // ── setIsReadAllMessagesInConversation ───────────────────

    @Test
    @DisplayName("setIsReadAllMessagesInConversation — делегирует в репозиторий сообщений")
    void setIsReadAllMessagesInConversation_delegates() {
        // Arrange
        doNothing().when(conversationMessageRepository)
                .setIsReadMessagesByConversationId(conversation, userFrom);

        // Act
        Conversation result = conversationsService.setIsReadAllMessagesInConversation(conversation, userFrom);

        // Assert
        assertThat(result).isSameAs(conversation);
        verify(conversationMessageRepository, times(1))
                .setIsReadMessagesByConversationId(conversation, userFrom);
    }

    // ── upConversation ────────────────────────────────────────

    @Test
    @DisplayName("upConversation — увеличивает orderWidth на 1 и сохраняет")
    void upConversation_incrementsAndSaves() {
        // Arrange
        conversation.setOrderWidth(5L);
        when(conversationRepository.save(any(Conversation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Conversation result = conversationsService.upConversation(conversation);

        // Assert
        assertThat(result.getOrderWidth()).isEqualTo(6L);
        verify(conversationRepository).save(conversation);
    }

    // ── downConversation ─────────────────────────────────────

    @Test
    @DisplayName("downConversation — уменьшает orderWidth на 1")
    void downConversation_decrements() {
        // Arrange
        conversation.setOrderWidth(5L);

        // Act
        Conversation result = conversationsService.downConversation(conversation);

        // Assert
        assertThat(result.getOrderWidth()).isEqualTo(4L);
        // Внимание: downConversation не вызывает save — это баг в сервисе
        verify(conversationRepository, never()).save(any());
    }

    @Test
    @DisplayName("downConversation — возвращает conversation без изменений, если orderWidth = 0")
    void downConversation_zeroStaysZero() {
        // Arrange
        conversation.setOrderWidth(0L);

        // Act
        Conversation result = conversationsService.downConversation(conversation);

        // Assert
        assertThat(result.getOrderWidth()).isEqualTo(0L);
    }

    @Test
    @DisplayName("downConversation — сбрасывает отрицательный orderWidth в 0, затем уменьшает до -1")
    void downConversation_negativeValue() {
        // Arrange
        conversation.setOrderWidth(-3L);

        // Act
        Conversation result = conversationsService.downConversation(conversation);

        // Assert
        // Метод сначала проверяет < 0, ставит 0L, затем декрементит → -1
        assertThat(result.getOrderWidth()).isEqualTo(-1L);
    }

    // ── checkConversationIsUser ──────────────────────────────

    @Test
    @DisplayName("checkConversationIsUser — true, если currentUser = userFrom")
    void checkConversationIsUser_userFrom() {
        // Act
        boolean result = conversationsService.checkConversationIsUser(userFrom, conversation);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("checkConversationIsUser — true, если currentUser = userTo")
    void checkConversationIsUser_userTo() {
        // Act
        boolean result = conversationsService.checkConversationIsUser(userTo, conversation);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("checkConversationIsUser — false, если currentUser не участвует в диалоге")
    void checkConversationIsUser_stranger() {
        // Arrange
        UserEntity stranger = new UserEntity();
        stranger.setId(999L);

        // Act
        boolean result = conversationsService.checkConversationIsUser(stranger, conversation);

        // Assert
        assertThat(result).isFalse();
    }

    // ── resetConversationWidth ───────────────────────────────

    @Test
    @DisplayName("resetConversationWidth — сбрасывает orderWidth в 0")
    void resetConversationWidth_setsZero() {
        // Arrange
        conversation.setOrderWidth(42L);

        // Act
        Conversation result = conversationsService.resetConversationWidth(conversation);

        // Assert
        assertThat(result.getOrderWidth()).isEqualTo(0L);
        // Метод не вызывает save
        verify(conversationRepository, never()).save(any());
    }
}
