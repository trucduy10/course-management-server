package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aptech.coursemanagementserver.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);// SELECT username FROM users WHERE email = email

    Optional<User> findById(long id);

    @Query(value = """
            SELECT u.* FROM users u
            WHERE u.role = 'USER'
                """, nativeQuery = true)
    List<User> findAllHasRoleUSER();

    @Query(value = """
            SELECT u.* FROM users u
            WHERE u.role <> 'USER'
                """, nativeQuery = true)
    List<User> findAllExceptRoleUSER();

    @Query(value = """
            SELECT u.* FROM users u
            WHERE u.role <> 'ADMIN' AND u.id <> :userId
                      """, nativeQuery = true)
    List<User> findAllExceptRoleADMIN(long userId);

}
