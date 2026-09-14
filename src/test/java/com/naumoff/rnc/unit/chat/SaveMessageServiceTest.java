package com.naumoff.rnc.unit.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.chat.ConversationMessageRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.chat.SaveMessageService;
import com.naumoff.rnc.services.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveMessageServiceTest {

    @Mock
    private ConversationsService conversationsService;

    @Mock
    private ConversationMessageRepository conversationMessageRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SaveMessageService saveMessageService;

    private UserEntity sender;
    private Conversation conversation;
    private ChatMessage chatMessage;

    @BeforeEach
    void setUp() {
        sender = new UserEntity();
        sender.setId(1L);
        sender.setEmail("sender@example.com");

        conversation = new Conversation();
        conversation.setId(100L);

        chatMessage = ChatMessage.builder()
                .text("Привет, как дела?")
                .conversationId(100L)
                .senderId(1L)
                .recipientId(2L)
                .build();
    }

    @Test
    @DisplayName("saveMessage — создаёт сообщение с корректными полями и сохраняет")
    void saveMessage_createsAndSavesMessage() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(sender);
        when(conversationMessageRepository.save(any(ConversationMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ConversationMessage result = saveMessageService.saveMessage(1L, conversation, chatMessage);

        // Assert
        ArgumentCaptor<ConversationMessage> captor = ArgumentCaptor.forClass(ConversationMessage.class);
        verify(conversationMessageRepository).save(captor.capture());

        ConversationMessage saved = captor.getValue();
        assertThat(saved.getMessage()).isEqualTo("Привет, как дела?");
        assertThat(saved.getConversation()).isEqualTo(conversation);
        assertThat(saved.getSender()).isEqualTo(sender);
        assertThat(saved.getIsRead()).isFalse();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        assertThat(result).isSameAs(saved);
    }

    @Test
    @DisplayName("saveMessage — вызывает upConversation для подъёма диалога")
    void saveMessage_callsUpConversation() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(sender);
        when(conversationMessageRepository.save(any(ConversationMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        saveMessageService.saveMessage(1L, conversation, chatMessage);

        // Assert
        verify(conversationsService, times(1)).upConversation(conversation);
    }

    @Test
    @DisplayName("saveMessage — запрашивает отправителя через UserService по id")
    void saveMessage_fetchesSenderFromUserService() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(sender);
        when(conversationMessageRepository.save(any(ConversationMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        saveMessageService.saveMessage(1L, conversation, chatMessage);

        // Assert
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("saveMessage — upConversation вызывается до save")
    void saveMessage_upConversationCalledBeforeSave() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(sender);

        // Используем InOrder для проверки последовательности
        var inOrder = inOrder(conversationsService, conversationMessageRepository);

        // Act
        saveMessageService.saveMessage(1L, conversation, chatMessage);

        // Assert
        inOrder.verify(conversationsService).upConversation(conversation);
        inOrder.verify(conversationMessageRepository).save(any(ConversationMessage.class));
    }

    @Test
    @DisplayName("saveMessage — createdAt и updatedAt заполняются текущим временем")
    void saveMessage_setsTimestamps() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(sender);
        when(conversationMessageRepository.save(any(ConversationMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        // Act
        ConversationMessage result = saveMessageService.saveMessage(1L, conversation, chatMessage);

        // Assert
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertThat(result.getCreatedAt()).isBetween(before, after);
        assertThat(result.getUpdatedAt()).isBetween(before, after);
    }

    @Test
    @DisplayName("saveMessage — isRead всегда false при создании")
    void saveMessage_isReadAlwaysFalse() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(sender);
        when(conversationMessageRepository.save(any(ConversationMessage.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ConversationMessage result = saveMessageService.saveMessage(1L, conversation, chatMessage);

        // Assert
        assertThat(result.getIsRead()).isFalse();
    }
}
