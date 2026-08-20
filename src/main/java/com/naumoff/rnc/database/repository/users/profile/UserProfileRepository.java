package com.naumoff.rnc.database.repository.users.profile;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    // findAll() уже есть в JpaRepository
    // Можно использовать напрямую: userProfileRepository.findAll()

    /**
     * Найти профиль по ID пользователя.
     * Это самый частый кейс: «дай профиль для текущего юзера».
     */
    Optional<UserProfileEntity> findByUserId(Long userId);

    /**
     * Альтернатива: поиск по сущности User (если удобнее передавать объект).
     */
    Optional<UserProfileEntity> findByUser(UserEntity user);
}
