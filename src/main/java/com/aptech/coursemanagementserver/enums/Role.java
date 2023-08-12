package com.aptech.coursemanagementserver.enums;

import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_CREATE;
import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_DELETE;
import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_READ;
import static com.aptech.coursemanagementserver.enums.Permission.ADMIN_UPDATE;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_CREATE;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_DELETE;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_READ;
import static com.aptech.coursemanagementserver.enums.Permission.MANAGER_UPDATE;
import static com.aptech.coursemanagementserver.enums.Permission.EMPLOYEE_READ;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

        USER,
        ADMIN,
        MANAGER,
        EMPLOYEE

}
