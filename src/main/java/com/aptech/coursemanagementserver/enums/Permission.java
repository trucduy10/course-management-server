package com.aptech.coursemanagementserver.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("ADMIN_READ"),
    ADMIN_UPDATE("ADMIN_UPDATE"),
    ADMIN_CREATE("ADMIN_CREATE"),
    ADMIN_DELETE("ADMIN_DELETE"),
    MANAGER_READ("MANAGER_READ"),
    MANAGER_UPDATE("MANAGER_UPDATE"),
    MANAGER_CREATE("MANAGER_CREATE"),
    MANAGER_DELETE("MANAGER_DELETE"),
    EMPLOYEE_READ("EMPLOYEE_READ"),

    ;

    @Getter
    private final String permission;
}
