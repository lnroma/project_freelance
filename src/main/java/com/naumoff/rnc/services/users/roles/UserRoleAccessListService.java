package com.naumoff.rnc.services.users.roles;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.role.AccessListEntity;
import com.naumoff.rnc.database.entities.users.role.UserRoleEntity;
import com.naumoff.rnc.database.repository.users.role.AccessListRepository;
import com.naumoff.rnc.simpleFactories.AtomicBooleanFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserRoleAccessListService {

    private final UserRoleService userRoleService;
    private final AccessListRepository accessListRepository;
    private final AtomicBooleanFactory atomicBooleanFactory;

    public UserRoleAccessListService(
            UserRoleService userRoleService,
            AccessListRepository accessListRepository,
            AtomicBooleanFactory atomicBooleanFactory
    ) {
        this.userRoleService = userRoleService;
        this.accessListRepository = accessListRepository;
        this.atomicBooleanFactory = atomicBooleanFactory;
    }

    public boolean hasAccessToMenu(UserEntity currentUser, String slug) {
        List<UserRoleEntity> roles = userRoleService.getUserRoles(currentUser);

        AtomicBoolean isAvailable = new AtomicBoolean(false);

        roles.forEach((role) -> {
            if (handleAccessByObjectTypeObjectKey("menu", slug, role.getId())) {
                isAvailable.set(true);
            }
        });

        return isAvailable.get();
    }

    public Boolean hasReadResource(UserEntity currentUser, String objectType, String objectKey) {
        List<UserRoleEntity> roles = userRoleService.getUserRoles(currentUser);

        AtomicBoolean isRead = atomicBooleanFactory.createAtomicBoolean(false);

        roles.forEach((role) -> {
            if (handleAccessByObjectTypeObjectKey(objectType, objectKey, role.getId())) {
                isRead.set(true);
            }
        });

        return isRead.get();
    }

    public Boolean hasReadResource(UserEntity currentUser, String objectType, Long objectId) {
        return true;
    }

    public boolean hasCreateResource(UserEntity currentUser, String objectType, String objectKey) {
        List<UserRoleEntity> roles = userRoleService.getUserRoles(currentUser);

        AtomicBoolean isCreated = atomicBooleanFactory.createAtomicBoolean(false);

        roles.forEach((role) -> {
            if (handleAccessByObjectTypeObjectKey(objectType, objectKey, role.getId())) {
                isCreated.set(true);
            }
        });

        return isCreated.get();
    }

    public boolean hasCreateResource(UserEntity currentUser, String objectType, Long objectId) {
        return true;
    }

    public boolean hasUpdateResource(UserEntity currentUser, String objectType, String objectKey) {
        List<UserRoleEntity> roles = userRoleService.getUserRoles(currentUser);

        AtomicBoolean isUpdate = atomicBooleanFactory.createAtomicBoolean(false);

        roles.forEach((role) -> {
            if (handleAccessByObjectTypeObjectKey(objectType, objectKey, role.getId())) {
                isUpdate.set(true);
            }
        });

        return isUpdate.get();
    }

    public boolean hasUpdateResource(UserEntity currentUser, String objectType, Long objectId) {
        List<UserRoleEntity> roles = userRoleService.getUserRoles(currentUser);

        AtomicBoolean isUpdate = atomicBooleanFactory.createAtomicBoolean(false);

        roles.forEach((role) -> {
            if (handleAccessByObjectTypeObjectKey(objectType, objectId, role.getId())) {
                isUpdate.set(true);
            }
        });

        return isUpdate.get();
    }

    public boolean hasResourceOwner(UserEntity currentUser, UserEntity ownerUser) {
        return currentUser.getId().equals(ownerUser.getId());
    }

    private Boolean handleAccessByObjectTypeObjectKey(String objectType, String objectKey, Long roleId) {
        boolean result = false;

        AccessListEntity accessListEntity = accessListRepository.findFirstByObjectTypeAndObjectKeyAndRoleId(
                objectType,
                objectKey,
                roleId
        );

        if (accessListEntity != null) {
            result = true;
        }

        return result;
    }

    private Boolean handleAccessByObjectTypeObjectKey(String objectType, Long resourceId, Long roleId) {
        boolean result = false;

        AccessListEntity accessListEntity = accessListRepository.findFirstByObjectTypeAndObjectIdAndRoleId(
                objectType,
                resourceId,
                roleId
        );

        if (accessListEntity != null) {
            result = true;
        }

        return result;
    }
}
