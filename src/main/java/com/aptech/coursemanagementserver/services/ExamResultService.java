package com.aptech.coursemanagementserver.services;

import java.io.IOException;
import java.util.List;

import com.aptech.coursemanagementserver.dtos.AccomplishmentsDto;
import com.aptech.coursemanagementserver.dtos.CertificateDto;
import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;
import com.aptech.coursemanagementserver.dtos.FinishExamRequestDto;
import com.aptech.coursemanagementserver.dtos.FinishExamResponseDto;
import com.aptech.coursemanagementserver.dtos.RetakeExamDto;

import net.sf.jasperreports.engine.JRException;

public interface ExamResultService {
    public int createExamResult(long userId, long courseId);

    public List<ExamResultResponseDto> findExamResultByCourseIdAndUserIdAndExamSession(long courseId, long userId,
            int examSession);

    public List<AccomplishmentsDto> findPassedExamResultByUserId(long userId);

    public FinishExamResponseDto finishExam(FinishExamRequestDto dto);

    public RetakeExamDto retakeExam(long userId, long courseId);

    public byte[] getCertificate(CertificateDto certificateDto) throws JRException, IOException;
}
