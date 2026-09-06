package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public UserEntity changeCity(UserEntity currentUser, Long cityId) {
        currentUser.setCityId(cityId);

        userRepository.save(currentUser);

        return currentUser;
    }
}
