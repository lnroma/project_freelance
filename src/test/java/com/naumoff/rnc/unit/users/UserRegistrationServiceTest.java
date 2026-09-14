package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.dto.users.UserDto;
import com.naumoff.rnc.services.users.UserRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setPhoneNumber("+79991234567");
        userDto.setPassword("secret123");
        userDto.setRole("USER");
        userDto.setCostPerMonth(new BigDecimal(100));
        userDto.setCityId(1L);
    }

    @Test
    @DisplayName("registerUser — успешно создаёт пользователя с закодированным паролем")
    void registerUser_success() {
        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded_password");
        when(passwordEncoder.encode("secret123test@example.com")).thenReturn("encoded_token");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userRegistrationService.registerUser(userDto);

        // Assert
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getPhoneNumber()).isEqualTo("+79991234567");
        assertThat(saved.getPassword()).isEqualTo("encoded_password");
        assertThat(saved.getRole()).isEqualTo("USER");
        assertThat(saved.getCostPerMonth()).isEqualTo(1500);
        assertThat(saved.getCityId()).isEqualTo(1L);
        assertThat(saved.getAuthToken()).isEqualTo("encoded_token");

        assertThat(result).isSameAs(saved);

        verify(passwordEncoder, times(1)).encode("secret123");
        verify(passwordEncoder, times(1)).encode("secret123test@example.com");
    }

    @Test
    @DisplayName("registerUser — бросает IllegalArgumentException, если email уже существует")
    void registerUser_emailAlreadyExists_throws() {
        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userRegistrationService.registerUser(userDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email уже зарегистрирован в базе данных");

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("registerUser — authToken формируется из пароля и email")
    void registerUser_authTokenIsEncodedFromPasswordAndEmail() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("enc_pass");
        when(passwordEncoder.encode("secret123test@example.com")).thenReturn("enc_token");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userRegistrationService.registerUser(userDto);

        // Assert
        assertThat(result.getAuthToken()).isEqualTo("enc_token");
        assertThat(result.getAuthToken()).isNotEqualTo(result.getPassword());
    }
}
