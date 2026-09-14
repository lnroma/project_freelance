package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.services.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setCityId(5L);
    }

    // ── getUserById ──────────────────────────────────────────

    @Test
    @DisplayName("getUserById — возвращает пользователя, если он найден")
    void getUserById_returnsUserWhenFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserEntity result = userService.getUserById(1L);

        // Assert
        assertThat(result).isSameAs(user);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getUserById — бросает NoSuchElementException, если пользователь не найден")
    void getUserById_throwsWhenNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(NoSuchElementException.class);

        verify(userRepository, times(1)).findById(99L);
    }

    // ── changeCity ───────────────────────────────────────────

    @Test
    @DisplayName("changeCity — обновляет cityId и сохраняет пользователя")
    void changeCity_updatesAndSaves() {
        // Arrange
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userService.changeCity(user, 10L);

        // Assert
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository, times(1)).save(captor.capture());

        UserEntity saved = captor.getValue();
        assertThat(saved.getCityId()).isEqualTo(10L);
        assertThat(result).isSameAs(saved);
    }

    @Test
    @DisplayName("changeCity — если новый cityId совпадает со старым, всё равно сохраняет")
    void changeCity_sameCityIdStillSaves() {
        // Arrange
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userService.changeCity(user, 5L);

        // Assert
        assertThat(result.getCityId()).isEqualTo(5L);
        verify(userRepository, times(1)).save(user);
    }
}
