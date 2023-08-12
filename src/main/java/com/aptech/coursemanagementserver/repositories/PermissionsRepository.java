package com.aptech.coursemanagementserver.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.Permissions;

public interface PermissionsRepository extends JpaRepository<Permissions, Long> {
    @Query(value = """
            SELECT p.* FROM permissions p
            INNER JOIN roles r ON p.role_id = r.id
            WHERE r.name <> 'ADMIN'
                      """, nativeQuery = true)
    List<Permissions> findAllPermissionExceptPermissionADMIN();

    Permissions findByPermission(String permission);
}
