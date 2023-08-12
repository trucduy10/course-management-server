package com.aptech.coursemanagementserver.dtos;

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
public class AuthorRequestDto {
    private long categoryId;
    private int pageNo;
    private int pageSize;
    private String searchKey;
    private String searchValue;
    private String sortKey;
    private String operation;
}
