package com.aptech.coursemanagementserver.dtos;

import java.util.ArrayList;
import java.util.List;

import com.aptech.coursemanagementserver.models.Permissions;
import com.aptech.coursemanagementserver.models.Roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionDto {
    private long userId;

    @Builder.Default
    private List<Permissions> permissions = new ArrayList<>();

    private Roles role;
}
