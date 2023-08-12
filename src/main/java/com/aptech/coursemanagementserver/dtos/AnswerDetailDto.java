package com.aptech.coursemanagementserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AnswerDetailDto {
    private long id;
    private long answerId;
    private long userAnswerId;
    private boolean correct;
    private String description;
    private long partId;
    private double point;
}
