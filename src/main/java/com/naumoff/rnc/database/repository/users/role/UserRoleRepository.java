package com.naumoff.rnc.database.repository.users.role;

import com.naumoff.rnc.database.entities.users.role.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity getFirstByKey(String key);
}
