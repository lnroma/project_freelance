package com.naumoff.rnc.database.repository.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>  {
    boolean existsByEmail(String email);
    boolean existsByAuthToken(String authToken);
    Optional<UserEntity> findByEmail(String email);
}
