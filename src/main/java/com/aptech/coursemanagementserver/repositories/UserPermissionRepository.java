package com.aptech.coursemanagementserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aptech.coursemanagementserver.models.UserPermission;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM user_permission
            WHERE user_id = :userId
                    """, nativeQuery = true)
    void deleteUserPermission(long userId);

}
