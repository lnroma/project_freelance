package com.naumoff.rnc.services.users.roles;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.role.UserRoleEntity;
import com.naumoff.rnc.database.repository.users.role.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(
            UserRoleRepository userRoleRepository
    ) {
        this.userRoleRepository = userRoleRepository;
    }

    final public String ROLE_ALL = "ALL";
    final public String ROLE_RESOURCE_OWNER = "RESOURCE_OWNER";
    final public String ROLE_USER = "USER";
    final public String ROLE_ADMIN = "ADMIN";
    final public String ROLE_MANAGER = "MANAGER";
    final public String ROLE_MODERATOR = "MODERATOR";

    public List<UserRoleEntity> getUserRoles(UserEntity currentUser) {
        List<UserRoleEntity> result = new ArrayList<>();

        if (currentUser == null) {
            result.add(userRoleRepository.getFirstByKey(ROLE_ALL));

            return result;
        }

        result.addAll(getRoleFromDataBase(currentUser));

        result.add(userRoleRepository.getFirstByKey(ROLE_ALL));

        return result;
    }

    public List<UserRoleEntity> getUserRoles(UserEntity currentUser, UserEntity resourceOwnerUser) {
        List<UserRoleEntity> result = getUserRoles(currentUser);

        if (currentUser.getId().equals(resourceOwnerUser.getId())) {
            result.add(userRoleRepository.getFirstByKey(ROLE_RESOURCE_OWNER));
        }

        return result;
    }

    private List<UserRoleEntity> getRoleFromDataBase(UserEntity currentUser) {
        return currentUser.getRoles();
    }
}
