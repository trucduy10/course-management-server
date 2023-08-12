package com.aptech.coursemanagementserver.dtos.baseDto;

import com.aptech.coursemanagementserver.enums.AntType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto {
    @Enumerated(EnumType.STRING)
    AntType type;
    String message;
}
