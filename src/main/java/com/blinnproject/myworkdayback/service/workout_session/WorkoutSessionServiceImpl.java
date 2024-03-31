package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

  private final WorkoutSessionRepository workoutSessionRepository;

  public WorkoutSessionServiceImpl(WorkoutSessionRepository workoutSessionRepository) {
    this.workoutSessionRepository = workoutSessionRepository;
  }

  @Override
  public List<WorkoutSession> findAllByDate(LocalDate startedAt, Long createdBy) {
    int dayOfWeek = startedAt.getDayOfWeek().getValue();
    Timestamp currentDate = Timestamp.valueOf(startedAt.atStartOfDay());
    return workoutSessionRepository.findAllSessionByDate(dayOfWeek, currentDate, createdBy);
  }
}
