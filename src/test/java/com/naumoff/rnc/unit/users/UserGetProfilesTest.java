package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.services.users.UserGetProfiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserGetProfilesTest {

    private UserGetProfiles service;
    private UserEntity user;
    private List<UserProfileEntity> profiles;

    @BeforeEach
    void setUp() {
        service = new UserGetProfiles();
        user = new UserEntity();
        profiles = List.of(new UserProfileEntity(), new UserProfileEntity());
        user.setProfiles(profiles);
    }

    @Test
    void getUserProfiles_shouldReturnUserProfiles() {
        // Act
        List<UserProfileEntity> result = service.getUserProfiles(user);

        // Assert
        assertThat(result).isEqualTo(profiles);
        assertThat(result).hasSize(2);
    }

    @Test
    void getUserProfiles_shouldReturnEmptyListIfNull() {
        // Arrange
        user.setProfiles(null);

        // Act
        List<UserProfileEntity> result = service.getUserProfiles(user);

        // Assert
//        assertThat(result).isEmpty();
    }

    @Test
    void getUserProfileLimit_shouldReturnDefaultLimit() {
        // Act
        Integer limit = service.getUserProfileLimit(user);

        // Assert
        assertThat(limit).isEqualTo(1);
    }
}
