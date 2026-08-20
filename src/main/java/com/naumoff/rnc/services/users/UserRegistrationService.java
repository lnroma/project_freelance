package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.dto.users.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(UserDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email уже зарегистрирован в базе данных");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setCostPerMonth(request.getCostPerMonth());
        user.setCityId(request.getCityId());
        user.setAuthToken(passwordEncoder.encode(request.getPassword() + request.getEmail()));

        return userRepository.save(user);
    }
}
