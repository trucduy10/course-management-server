package com.aptech.coursemanagementserver.services.servicesImpl;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.models.Permissions;
import com.aptech.coursemanagementserver.models.Roles;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.models.UserPermission;
import com.aptech.coursemanagementserver.repositories.PermissionsRepository;
import com.aptech.coursemanagementserver.repositories.RolesRepository;
import com.aptech.coursemanagementserver.repositories.UserPermissionRepository;
import com.aptech.coursemanagementserver.repositories.UserRepository;
import com.aptech.coursemanagementserver.services.authServices.UserPermissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {
    private final UserRepository userRepository;
    private final RolesRepository roleRepository;
    private final PermissionsRepository permissionsRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Override
    public void saveRole(Roles role) {
        roleRepository.save(role);
    }

    public void savePermission(Permissions permission) {
        permissionsRepository.save(permission);
    }

    @Override
    public void saveUserPermission(Permissions permission, User user) {

        UserPermission userPermission = new UserPermission();
        userPermission.setPermission(permission)
                .setPermissionName(permission.getPermission())
                .setUser(user).setUsername(user.getUsername());

        userPermissionRepository.save(userPermission);
    }

    @Override
    public Permissions findByPermission(String permission) {
        // TODO Auto-generated method stub
        return permissionsRepository.findByPermission(permission);
    }

}
