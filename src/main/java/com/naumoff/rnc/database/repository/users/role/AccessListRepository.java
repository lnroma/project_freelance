package com.naumoff.rnc.database.repository.users.role;

import com.naumoff.rnc.database.entities.users.role.AccessListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessListRepository extends JpaRepository<AccessListEntity, Long> {

    AccessListEntity findFirstByObjectTypeAndObjectKeyAndRoleId(
            String objectType,
            String objectId,
            Long roleId
    );

    AccessListEntity findFirstByObjectTypeAndObjectIdAndRoleId(
            String objectType,
            Long objectId,
            Long roleId
    );
}
