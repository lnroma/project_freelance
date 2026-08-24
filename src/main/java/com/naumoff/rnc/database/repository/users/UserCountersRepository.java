package com.naumoff.rnc.database.repository.users;

import com.naumoff.rnc.database.entities.users.UserCountersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCountersRepository extends JpaRepository<UserCountersEntity, Long> {
}
