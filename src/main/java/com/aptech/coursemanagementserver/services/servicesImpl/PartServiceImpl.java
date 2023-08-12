package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.PartDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Part;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.ExamResultRepository;
import com.aptech.coursemanagementserver.repositories.PartRepository;
import com.aptech.coursemanagementserver.repositories.QuestionRepository;
import com.aptech.coursemanagementserver.services.PartService;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final ExamResultRepository examResultRepository;
    private final UserService userService;

    @Override
    public PartDto findById(long id) {
        Part part = partRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "This part with partId: [" + id + "] is not exist."));

        return toDto(part);
    }

    @Override
    public List<PartDto> findAllByCourseId(long courseId) {
        List<Part> parts = partRepository.findByCourseId(courseId);

        List<PartDto> partDtos = new ArrayList<>();

        for (Part part : parts) {
            PartDto partDto = toDto(part);
            partDtos.add(partDto);
        }

        return partDtos;
    }

    @Override
    public void save(PartDto partDto) {
        Part part = new Part();

        // Update part
        if (partDto.getId() > 0) {
            part = partRepository.findById(partDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This part with partId: [" + partDto.getId() + "] is not exist."));

            /// bool checkIsUpdatedMaxPoint = false

            if (examResultRepository.findByPartId(part.getId()).size() > 0) {
                throw new BadRequestException("The Part 've already registered in examination.");
            }
            if (partDto.getMaxPoint() != part.getMaxPoint()) {
                questionRepository.updatePointWhenMaxPointReduce(part.getId());
            }
            if (partDto.getStatus() == 1) {

                if (part.getQuestions().size() == 0) {
                    throw new BadRequestException("Cannot activate part that doesn't contain any question.");
                } else {
                    Optional<Double> questionsPoint = part.getQuestions().stream().map(q -> q.getPoint())
                            .reduce((a, b) -> a + b);
                    if (questionsPoint.isPresent() && questionsPoint.get() < partDto.getMaxPoint()) {
                        throw new BadRequestException("Total point of questions must be equal part's max point.");
                    } else if (questionsPoint.isPresent() && questionsPoint.get() > partDto.getMaxPoint()) {
                        partDto.setStatus(0);
                    }

                    StringBuilder invalidQuestions = new StringBuilder();
                    part.getQuestions().forEach(q -> {
                        if (q.getAnswers().size() < 4) {
                            invalidQuestions.append(" + " + q.getDescription() + "\n");
                        }
                    });
                    if (invalidQuestions.length() > 0) {
                        throw new BadRequestException(
                                "Question of part don't have enough answers.");
                    }
                }

            }

        }

        Course course = courseRepository.findById(partDto.getCourseId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This course with courseId: [" + partDto.getCourseId() + "] is not exist."));
        User user = userService.findCurrentUser();
        part.setCourse(course);
        part.setLimitTime(partDto.getLimitTime());
        part.setMaxPoint(partDto.getMaxPoint());
        part.setStatus(partDto.getStatus());
        part.setUpdatedBy(user.getEmail().split("@")[0]);
        partRepository.save(part);
    }

    @Override
    public void deletePart(long partId) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new NoSuchElementException("This part with partId: [" + partId + "] is not exist."));

        if (part.getQuestions().size() > 0) {
            throw new BadRequestException("The part 've already had question.");
        }

        if (examResultRepository.findByPartId(partId).size() > 0) {
            throw new BadRequestException("The Part 've already registered in examination.");
        }
        partRepository.delete(part);
    }

    private PartDto toDto(Part part) {
        PartDto partDto = PartDto.builder()
                .id(part.getId())
                .maxPoint(part.getMaxPoint())
                .limitTime(part.getLimitTime())
                .courseId(part.getCourse().getId())
                .status(part.getStatus())
                .updatedBy(part.getUpdatedBy())
                .build();
        return partDto;
    }
}
