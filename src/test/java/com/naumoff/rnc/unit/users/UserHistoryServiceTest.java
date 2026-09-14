package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserHistory;
import com.naumoff.rnc.database.repository.users.history.UserHistoryRepository;
import com.naumoff.rnc.services.users.UserHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHistoryServiceTest {

    @Mock
    private UserHistoryRepository userHistoryRepository;

    @InjectMocks
    private UserHistoryService userHistoryService;

    private UserEntity currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new UserEntity();
        currentUser.setId(1L);
    }

    @Test
    void createHistoryRecord_simple_shouldCreateAndSaveHistory() {
        String title = "Вход в систему";
        String description = "Пользователь вошёл в личный кабинет";

        UserHistory savedHistory = mock(UserHistory.class);
        when(userHistoryRepository.save(any(UserHistory.class))).thenReturn(savedHistory);

        UserHistory result = userHistoryService.createHistoryRecord(title, description, currentUser);

        assertThat(result.getDescription()).isEqualTo(savedHistory.getDescription());

        ArgumentCaptor<UserHistory> captor = ArgumentCaptor.forClass(UserHistory.class);
        verify(userHistoryRepository).save(captor.capture());

        UserHistory captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo(title);
        assertThat(captured.getDescription()).isEqualTo(description);
        assertThat(captured.getUser()).isEqualTo(currentUser);
        assertThat(captured.getObjectType()).isNull();
        assertThat(captured.getObjectId()).isNull();
        assertThat(captured.getCreatedAt()).isNotNull();
        assertThat(captured.getUpdatedAt()).isNotNull();
    }

    @Test
    void createHistoryRecord_withObjectTypeAndObjectId_shouldSetExtraFields() {
        String title = "Заказ создан";
        String description = "Создан заказ №123";
        String objectType = "ORDER";
        Long objectId = 123L;

        UserHistory savedHistory = mock(UserHistory.class);
        when(userHistoryRepository.save(any(UserHistory.class))).thenReturn(savedHistory);

        UserHistory result = userHistoryService.createHistoryRecord(
                title, description, currentUser, objectType, objectId
        );

        assertThat(result).isEqualTo(savedHistory);

        ArgumentCaptor<UserHistory> captor = ArgumentCaptor.forClass(UserHistory.class);
        verify(userHistoryRepository).save(captor.capture());

        UserHistory captured = captor.getValue();
        assertThat(captured.getTitle()).isEqualTo(title);
        assertThat(captured.getDescription()).isEqualTo(description);
        assertThat(captured.getUser()).isEqualTo(currentUser);
        assertThat(captured.getObjectType()).isEqualTo(objectType);
        assertThat(captured.getObjectId()).isEqualTo(objectId);
        assertThat(captured.getCreatedAt()).isNotNull();
        assertThat(captured.getUpdatedAt()).isNotNull();
    }

    @Test
    void createHistoryRecord_shouldSetTimestampsAroundNow() {
        String title = "Тест базовой истории";
        String description = "Проверка создания базовой записи";

        LocalDateTime before = LocalDateTime.now();

        UserHistory savedHistory = mock(UserHistory.class);
        when(userHistoryRepository.save(any(UserHistory.class))).thenReturn(savedHistory);

        userHistoryService.createHistoryRecord(title, description, currentUser);

        ArgumentCaptor<UserHistory> captor = ArgumentCaptor.forClass(UserHistory.class);
        verify(userHistoryRepository).save(captor.capture());

        UserHistory captured = captor.getValue();
        LocalDateTime created = captured.getCreatedAt();
        LocalDateTime updated = captured.getUpdatedAt();

        // Проверяем, что даты близки к моменту вызова (в пределах 5 секунд)
        assertThat(created).isBetween(before.minusSeconds(5), before.plusSeconds(5));
        assertThat(updated).isBetween(before.minusSeconds(5), before.plusSeconds(5));
    }

    @Test
    void createHistoryRecord_shouldCallSaveExactlyOnce() {
        String title = "Ещё один тест";
        String description = "Убедимся, что save вызывается 1 раз";

        UserHistory savedHistory = mock(UserHistory.class);
        when(userHistoryRepository.save(any(UserHistory.class))).thenReturn(savedHistory);

        userHistoryService.createHistoryRecord(title, description, currentUser);

        verify(userHistoryRepository, times(1)).save(any(UserHistory.class));
    }
}
