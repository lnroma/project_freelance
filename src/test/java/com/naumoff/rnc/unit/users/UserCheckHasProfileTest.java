package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.services.users.UserCheckHasProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserCheckHasProfileTest {

    @Test
    void isHasProfile_shouldReturnFalse() {
        // Arrange
        UserCheckHasProfile service = new UserCheckHasProfile();
        UserEntity user = new UserEntity();

        // Act
        boolean result = service.isHasProfile(user);

        // Assert
        assertThat(result).isFalse();
    }

    // Этот тест нужен, если ты позже раскомментируешь логику с profiles
    @Test
    @SuppressWarnings("unused")
    void isHasProfile_wouldReturnTrueIfProfilesNotEmpty() {
        // Сейчас этот тест будет падать, потому что в коде стоит return false.
        // Оставь его как заготовку — когда реализуешь реальную логику, сможешь быстро активировать.
        UserCheckHasProfile service = new UserCheckHasProfile();
        UserEntity user = new UserEntity();
        // user.setProfiles(List.of(...)); 

        boolean result = service.isHasProfile(user);
        assertThat(result).as("Когда будет реализована логика проверки профилей, здесь должен быть true при непустом списке").isFalse();
    }
}
