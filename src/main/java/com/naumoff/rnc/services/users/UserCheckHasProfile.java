package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserCheckHasProfile {
    public boolean isHasProfile(UserEntity user) {
//        return !user.getProfiles().isEmpty();
        return false;
    }
}
