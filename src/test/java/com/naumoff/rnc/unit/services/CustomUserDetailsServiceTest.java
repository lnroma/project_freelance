package com.naumoff.rnc.unit.services;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encoded_password");
        userEntity.setRole("ADMIN");
    }

    @Test
    @DisplayName("loadUserByUsername — возвращает AuthenticatedUser с корректными authorities")
    void loadUserByUsername_success() {
        // Arrange
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(userEntity));

        // Act
        var result = customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertThat(result).isInstanceOf(AuthenticatedUser.class);
        assertThat(result.getUsername()).isEqualTo("test@example.com");

        var expectedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        assertThat(result.getAuthorities()).isEqualTo(expectedAuthorities);

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("loadUserByUsername — обрезает пробелы в email перед поиском")
    void loadUserByUsername_trimsUsername() {
        // Arrange
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(userEntity));

        // Act
        var result = customUserDetailsService.loadUserByUsername("  test@example.com  ");

        // Assert
        assertThat(result.getUsername()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("loadUserByUsername — бросает UsernameNotFoundException, если пользователь не найден")
    void loadUserByUsername_userNotFound_throws() {
        // Arrange
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("nobody@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Не найден");

        verify(userRepository, times(1)).findByEmail("nobody@example.com");
    }

    @Test
    @DisplayName("loadUserByUsername — префикс ROLE_ добавляется к роли пользователя")
    void loadUserByUsername_rolePrefixIsAdded() {
        // Arrange
        userEntity.setRole("USER");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(userEntity));

        // Act
        var result = customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertThat(result.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
    }
}
