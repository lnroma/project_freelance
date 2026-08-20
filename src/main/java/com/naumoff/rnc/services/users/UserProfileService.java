package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import com.naumoff.rnc.database.repository.users.profile.UserProfileRepository;
import com.naumoff.rnc.dto.users.ProfileForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(
            UserProfileRepository userProfileRepository
    ) {
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * save profile
     *
     * @param profileForm profile form dto
     * @param user user dto
     * @return entity
     */
    public UserProfileEntity saveProfile(
            ProfileForm profileForm,
            UserEntity user
    ) {
        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUser(user);
        userProfileEntity.setAboutMe("");
        userProfileEntity.setBirthDate(profileForm.getBirthDate());
        userProfileEntity.setFirstName(profileForm.getFirstName());
        userProfileEntity.setLastName(profileForm.getLastName());
        userProfileEntity.setNickName(profileForm.getNickname());

        userProfileRepository.save(userProfileEntity);

        return userProfileEntity;
    }

    /**
     * Get current user profile
     *
     * @param user current user or user with profile
     * @returnt user profile entity
     */
    public UserProfileEntity getCurrentUserProfile(
            UserEntity user
    ) {
        return user.getProfiles().stream().findFirst().get();
    }
}