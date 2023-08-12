package com.aptech.coursemanagementserver.services.schedules;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.repositories.EnrollmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingPointScheduledTask {
  private final EnrollmentRepository enrollmentRepository;

  @Scheduled(fixedRate = 10000)
  public void computeRating() throws InterruptedException {
    enrollmentRepository.ratingProcess();
  }
}
