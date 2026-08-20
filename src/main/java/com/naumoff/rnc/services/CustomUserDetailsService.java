package com.naumoff.rnc.services;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.model.AuthenticatedUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username + " user name");
        UserEntity user = userRepository.findByEmail(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Не найден"));

        var authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return new AuthenticatedUser(user, authorities);
    }
}