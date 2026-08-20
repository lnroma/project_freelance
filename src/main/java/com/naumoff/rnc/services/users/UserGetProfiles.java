package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;

import java.util.List;

public class UserGetProfiles {

    public List<UserProfileEntity> getUserProfiles(UserEntity user) {
        return user.getProfiles();
    }

    public Integer getUserProfileLimit(UserEntity user) {
        // @todo check tariffs
        return 1;
    }
}
