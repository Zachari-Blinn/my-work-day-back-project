package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface WorkoutSessionService {
  List<WorkoutSession> findAllByDate(LocalDate startedAt, Long createdBy);

}
