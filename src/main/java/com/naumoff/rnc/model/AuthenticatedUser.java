package com.naumoff.rnc.model;

import com.naumoff.rnc.database.entities.users.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AuthenticatedUser implements UserDetails {
    private final UserEntity entity;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(UserEntity entity, Collection<? extends GrantedAuthority> authorities) {
        this.entity = entity;
        this.authorities = authorities;
    }

    // делегируем стандартные методы UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return entity.getPassword();
    }

    @Override
    public String getUsername() {
        return String.valueOf(entity.getId());
    }

    public String getName() {
        return getUsername(); // или String.valueOf(entity.getId())
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // Твой метод, чтобы достать полную сущность
    public UserEntity getEntity() {
        return entity;
    }
}
