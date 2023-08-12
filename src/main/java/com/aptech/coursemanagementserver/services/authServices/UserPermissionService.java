package com.aptech.coursemanagementserver.services.authServices;

import com.aptech.coursemanagementserver.models.Permissions;
import com.aptech.coursemanagementserver.models.Roles;
import com.aptech.coursemanagementserver.models.User;

public interface UserPermissionService {
    public void saveRole(Roles role);

    public void savePermission(Permissions permission);

    public Permissions findByPermission(String permission);

    public void saveUserPermission(Permissions permission, User user);
}
