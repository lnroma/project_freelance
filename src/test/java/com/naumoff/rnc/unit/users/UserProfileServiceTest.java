package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.database.repository.users.profile.UserProfileRepository;
import com.naumoff.rnc.dto.users.ProfileForm;
import com.naumoff.rnc.services.users.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserEntity user;
    private ProfileForm profileForm;

    @BeforeEach
    void setUp() {
        user = new UserEntity();

        profileForm = new ProfileForm();
        profileForm.setFirstName("Иван");
        profileForm.setLastName("Иванов");
        profileForm.setNickname("ivanov");
        profileForm.setBirthDate(LocalDate.of(1990, 5, 15));
    }

    @Test
    @DisplayName("saveProfile — сохраняет сущность с корректными данными из формы")
    void saveProfile_setsAllFieldsAndSaves() {
        // Arrange
        when(userProfileRepository.save(any(UserProfileEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserProfileEntity result = userProfileService.saveProfile(profileForm, user);

        // Assert
        ArgumentCaptor<UserProfileEntity> captor = ArgumentCaptor.forClass(UserProfileEntity.class);
        verify(userProfileRepository, times(1)).save(captor.capture());

        UserProfileEntity saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getFirstName()).isEqualTo("Иван");
        assertThat(saved.getLastName()).isEqualTo("Иванов");
        assertThat(saved.getNickName()).isEqualTo("ivanov");
        assertThat(saved.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(saved.getAboutMe()).isEmpty();

        assertThat(result).isSameAs(saved);
    }

    @Test
    @DisplayName("getCurrentUserProfile — возвращает профиль, если он существует")
    void getCurrentUserProfile_returnsProfileWhenExists() {
        // Arrange
        UserProfileEntity profile = new UserProfileEntity();
        profile.setUser(user);
        profile.setFirstName("Иван");

        when(user.getProfiles()).thenReturn(List.of(profile));

        // Act
        UserProfileEntity result = userProfileService.getCurrentUserProfile(user);

        // Assert
        assertThat(result).isSameAs(profile);
        verifyNoInteractions(userProfileRepository);
    }

    @Test
    @DisplayName("getCurrentUserProfile — возвращает null, если профиль не существует")
    void getCurrentUserProfile_returnsNullWhenNoProfile() {
        // Arrange
        when(user.getProfiles()).thenReturn(List.of());

        // Act
        UserProfileEntity result = userProfileService.getCurrentUserProfile(user);

        // Assert
        assertThat(result).isNull();
        verifyNoInteractions(userProfileRepository);
    }
}
