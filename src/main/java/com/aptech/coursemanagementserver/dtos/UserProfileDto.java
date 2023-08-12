package com.aptech.coursemanagementserver.dtos;

import java.time.Instant;
import java.util.List;

import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserProfileDto extends BaseDto {
    private long id;
    private String name;
    private String first_name;
    private String last_name;
    private String email;
    private String imageUrl;
    private Role role;
    private List<String> permissions;
    private int status;
    private boolean isNotify;
    private Instant created_at;

}
