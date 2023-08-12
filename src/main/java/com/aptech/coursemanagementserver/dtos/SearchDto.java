package com.aptech.coursemanagementserver.dtos;

import com.aptech.coursemanagementserver.enums.SearchType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class SearchDto {
    private long id;
    private String name;
    private String description;
    private String image;
    private SearchType type;
}
