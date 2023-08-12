package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.AnswerDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Answer;
import com.aptech.coursemanagementserver.models.Question;
import com.aptech.coursemanagementserver.models.User;
import com.aptech.coursemanagementserver.repositories.AnswerRepository;
import com.aptech.coursemanagementserver.repositories.ExamResultRepository;
import com.aptech.coursemanagementserver.repositories.QuestionRepository;
import com.aptech.coursemanagementserver.services.AnswerService;
import com.aptech.coursemanagementserver.services.authServices.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final ExamResultRepository examResultRepository;
    private final UserService userService;

    @Override
    public AnswerDto findById(long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "This answer with answerId: [" + id + "] is not exist."));

        return toDto(answer);
    }

    @Override
    public List<AnswerDto> findAllByQuestionId(long questionId) {
        List<Answer> answers = answerRepository.findByQuestionId(questionId);
        List<AnswerDto> answerDtos = new ArrayList<>();

        for (Answer answer : answers) {
            AnswerDto answerDto = toDto(answer);
            answerDtos.add(answerDto);
        }

        return answerDtos;
    }

    @Override
    public void save(AnswerDto answerDto) {
        User user = userService.findCurrentUser();
        Question question = questionRepository.findById(answerDto.getQuestionId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This question with questionId: [" + answerDto.getQuestionId() + "] is not exist."));

        Answer answer = new Answer();
        var correctAnswer = question.getAnswers().stream().filter(a -> a.isCorrect() == true).findFirst();

        if (answerDto.getId() > 0) {
            // Update
            answer = answerRepository.findById(answerDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This answer with answerId: [" + answerDto.getId() + "] is not exist."));

            if (answerDto.isCorrect() == true) {
                question.getAnswers().forEach(a -> a.setCorrect(false));
                questionRepository.save(question);
            } else if (answerDto.isCorrect() == false && answer.isCorrect() == true) {
                throw new BadRequestException("There's must be one correct answer in one question.");
            }

        } else {
            // Add new
            int answerCount = question.getAnswers().size() + 1;
            // Số câu tl > 4
            if (answerCount > 4) {
                throw new BadRequestException("There's only 4 options in one question");
            }
            // So cau tra loi dung > 1
            if (correctAnswer.isPresent() && answerDto.isCorrect() == true) {
                throw new BadRequestException("There's only one correct answer in one question.");
            }
            if (answerCount == 4) {
                if (question.getAnswers().stream().anyMatch(a -> a.isCorrect() == true) == false
                        && answerDto.isCorrect() == false) {
                    throw new BadRequestException("There's must be one correct answer in one question.");
                }
                question.setFullAnswer(true);
                questionRepository.save(question);
            }
        }

        answer.setDescription(answerDto.getDescription());
        answer.setCorrect(answerDto.isCorrect());
        answer.setQuestion(question);
        answer.setUpdatedBy(user.getEmail().split("@")[0]);
        answerRepository.save(answer);

    }

    @Override
    public void deleteAnswer(long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(
                () -> new NoSuchElementException("This answer with answerId: [" + answerId + "] is not exist."));

        if (answer.getQuestion().getPart().getStatus() == 1) {
            throw new BadRequestException(
                    "Cannot delete answer within an activated part");
        }

        if (examResultRepository.findByAnswerId(answerId).size() > 0) {
            throw new BadRequestException(
                    "The answer 've already registered in examination.");
        }

        answerRepository.delete(answer);
        Question question = questionRepository.findById(answer.getQuestion().getId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This question with questionId: [" + answer.getQuestion().getId() + "] is not exist."));
        question.setFullAnswer(false);
        questionRepository.save(question);
    }

    @Override
    public AnswerDto toDto(Answer answer) {
        AnswerDto answerDto = AnswerDto.builder()
                .id(answer.getId())
                .description(answer.getDescription())
                .isCorrect(answer.isCorrect())
                .questionId(answer.getQuestion().getId())
                .updatedBy(answer.getUpdatedBy())
                .build();
        return answerDto;
    }
}
