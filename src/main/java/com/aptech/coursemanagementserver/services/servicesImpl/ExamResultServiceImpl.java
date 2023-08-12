package com.aptech.coursemanagementserver.services.servicesImpl;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AccomplishmentsDto;
import com.aptech.coursemanagementserver.dtos.AnswerDetailDto;
import com.aptech.coursemanagementserver.dtos.CertificateDto;
import com.aptech.coursemanagementserver.dtos.ExamResultResponseDto;
import com.aptech.coursemanagementserver.dtos.FinishExamRequestDto;
import com.aptech.coursemanagementserver.dtos.FinishExamResponseDto;
import com.aptech.coursemanagementserver.dtos.QuestionDto;
import com.aptech.coursemanagementserver.dtos.RetakeExamDto;
import com.aptech.coursemanagementserver.enums.GradeType;
import com.aptech.coursemanagementserver.models.Answer;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.ExamResult;
import com.aptech.coursemanagementserver.models.Part;
import com.aptech.coursemanagementserver.models.Question;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.ExamResultRepository;
import com.aptech.coursemanagementserver.repositories.PartRepository;
import com.aptech.coursemanagementserver.services.AnswerService;
import com.aptech.coursemanagementserver.services.ExamResultService;
import com.aptech.coursemanagementserver.services.QuestionService;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {
    private final ExamResultRepository examResultRepository;
    private final PartRepository partRepository;
    private final CourseRepository courseRepository;
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Override
    public int createExamResult(long userId, long courseId) {
        List<Part> parts = partRepository.findActivePartByCourseId(courseId);
        Collections.shuffle(parts);

        return examResultRepository.createExamResultByPartIdAndUserIdAndCourseId(
                parts.size() > 0 ? parts.get(0).getId() : 0, userId, courseId);
    }

    @Override
    public List<ExamResultResponseDto> findExamResultByCourseIdAndUserIdAndExamSession(long courseId, long userId,
            int examSession) {
        List<ExamResult> examResults = examResultRepository.findExamResultByCourseIdAndUserIdAndExamSession(courseId,
                userId,
                examSession);

        List<ExamResultResponseDto> examResultDtos = new ArrayList<>();
        Set<Question> questions = examResults.stream().map(e -> e.getQuestion()).collect(Collectors.toSet());
        List<Question> questionsList = new ArrayList<>(questions);
        Collections.shuffle(questionsList);

        for (Question question : questionsList) {

            QuestionDto questionDto = questionService.toDto(question);
            ExamResultResponseDto dto = ExamResultResponseDto.builder()
                    .question(questionDto)
                    .examSession(examSession)
                    .userId(userId)
                    .limitTime(question.getPart().getLimitTime())
                    .courseId(courseId).build();
            Set<Answer> answers = examResults.stream().filter(q -> q.getQuestion().equals(question))
                    .map(e -> e.getAnswer()).collect(Collectors.toSet());
            answers.forEach(a -> {
                dto.getAnswers().add(answerService.toDto(a));
            });

            examResultDtos.add(dto);
        }

        return examResultDtos;
    }

    @Override
    public FinishExamResponseDto finishExam(FinishExamRequestDto dto) {
        List<ExamResult> examResults = examResultRepository.findExamResultByCourseIdAndUserIdAndExamSession(
                dto.getCourseId(),
                dto.getUserId(),
                dto.getExamSession());

        // Tính tổng point answer correct
        var trueAnwser = dto.getAnswers().stream()
                .filter(a -> a.getUserAnswerId() == a.getAnswerId() && a.isCorrect() == true).toList();

        List<Double> trueAnwserPoint = trueAnwser.stream().map(a -> a.getPoint()).toList();
        double totalPoint = trueAnwserPoint.size() == 0 ? 0 : trueAnwserPoint.stream().reduce((a, b) -> a + b).get();

        long totalQuestion = examResults.stream().map(e -> e.getQuestion().getId()).distinct().count();
        // double totalPoint = 97.5;
        double maxPoint = examResults.get(0).getPart().getMaxPoint();
        double percentPoint = totalPoint * 100 / maxPoint;
        // 0-40: FAIL 40-65: AVERAGE 65-80: GOOD >=80: EXCELLENT
        GradeType grade = GradeType.FAIL;
        if (percentPoint >= 0 && percentPoint < 40) {
            grade = GradeType.FAIL;
        } else if (percentPoint >= 40 && percentPoint < 65) {
            grade = GradeType.AVERAGE;
        } else if (percentPoint >= 65 && percentPoint < 80) {
            grade = GradeType.GOOD;
        } else {
            grade = GradeType.EXCELLENT;
        }

        final String uid = examResults.get(0).generateGuiName();
        final Instant finishTime = Instant.now();

        for (AnswerDetailDto answer : dto.getAnswers()) {
            final GradeType gradeType = grade;
            examResults.forEach(e -> {
                if (e.getQuestion().getId() == answer.getId()) {
                    e.setUserAnswerId(answer.getUserAnswerId());
                }

                e.setTotalExamTime(dto.getTotalExamTime())
                        .setTotalPoint(totalPoint)
                        .setGrade(gradeType)
                        .setCreated_at(finishTime);
                if (gradeType != GradeType.FAIL) {

                    e.setCertificateUID(uid);
                }
            });
        }
        // for (ExamResult examResult : examResults) {
        // try {
        // examResult.setTotalExamTime(dto.getTotalExamTime())
        // .setTotalPoint(totalPoint)
        // .setGrade(grade);
        // } catch (Exception e) {
        // log.error("", e);
        // }

        // }
        examResultRepository.saveAll(examResults);

        FinishExamResponseDto finishDto = new FinishExamResponseDto();

        finishDto.setCreated_at(finishTime);
        finishDto.setCorrectAnswer(trueAnwser.size() + "/" + totalQuestion);

        finishDto.setTotalExamTime(dto.getTotalExamTime())
                .setTotalPoint(totalPoint)
                .setGrade(grade);
        return finishDto;
    }

    @Override
    public RetakeExamDto retakeExam(long userId, long courseId) {
        List<ExamResult> examResults = examResultRepository.findExamResultByCourseIdAndUserId(courseId, userId);
        RetakeExamDto retakeExamDto = new RetakeExamDto();

        var trueAnwser = examResults.stream()
                .filter(e -> e.getAnswer().getId() == e.getUserAnswerId() && e.isCorrect()
                        && e.getExamSession() == examResults.get(0).getExamSession())
                .toList();
        long totalQuestion = examResults.stream().map(e -> e.getQuestion().getId()).distinct().count();

        if (examResults.size() > 0) {
            boolean isPassed = examResults.stream().anyMatch(e -> e.getCertificateUID() != null);
            retakeExamDto.setPassed(isPassed);
            retakeExamDto.setCreated_at(examResults.get(0).getCreated_at());
            retakeExamDto.setExamSession(examResults.get(0).getExamSession());
            retakeExamDto.setCorrectAnswer(trueAnwser.size() + "/" + totalQuestion);
            retakeExamDto.setTotalExamTime(examResults.get(0).getTotalExamTime());
            retakeExamDto.setTotalPoint(examResults.get(0).getTotalPoint());
            retakeExamDto.setGrade(examResults.get(0).getGrade());
            return retakeExamDto;
        }

        return retakeExamDto;
    }

    @Override
    public List<AccomplishmentsDto> findPassedExamResultByUserId(long userId) {
        List<ExamResult> examResults = examResultRepository.findPassedExamResultByUserId(userId);
        List<AccomplishmentsDto> accomplishmentsDtos = new ArrayList<>();

        List<Course> courses = examResults.stream().map(e -> e.getCourse()).distinct().toList();

        for (Course course : courses) {
            if (examResults.size() > 0) {
                int courseTotalEnroll = courseRepository.findCourseTotalEnrolls(course.getId());
                List<ExamResult> filterExamResults = examResults.stream().filter(e -> e.getCourse() == course).toList();
                if (filterExamResults.size() > 0) {
                    AccomplishmentsDto accomplishmentsDto = new AccomplishmentsDto();
                    accomplishmentsDto.setCourseId(course.getId())
                            .setCourseImage(course.getImage())
                            .setCourseName(course.getName())
                            .setCourseSlug(course.getSlug())
                            .setCourseDuration(course.getDuration())
                            .setCourseTotalEnroll(courseTotalEnroll)
                            .setCourseRating(course.getRating())
                            .setCategoryName(course.getCategory().getName())
                            .setCategorySlug(course.getCategory().getSlug())
                            .setGrade(filterExamResults.get(0).getGrade())
                            .setCreated_at(filterExamResults.get(0).getCreated_at())
                            .setExamSession(filterExamResults.get(0).getExamSession())
                            .setCertificateUID(filterExamResults.get(0).getCertificateUID());
                    accomplishmentsDtos.add(accomplishmentsDto);
                }
            }
        }

        return accomplishmentsDtos;
    }

    public byte[] getCertificate(CertificateDto certificateDto) throws JRException, IOException {

        JasperReport jasperReport = JasperCompileManager
                .compileReport(new ClassPathResource("reports/Certificate_Landscape.jrxml").getInputStream());

        List<CertificateDto> certificateDtos = new ArrayList<>();
        certificateDtos.add(certificateDto);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(certificateDtos);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
