package com.blinnproject.myworkdayback.service.schedule;

import com.blinnproject.myworkdayback.model.dto.WorkoutSessionsScheduleDTO;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleView;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
  void delete(Long scheduleId, Long createdBy);

  List<WorkoutScheduleView> getWorkoutScheduleForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy);

  List<WorkoutSessionsScheduleDTO> getWorkoutSessionsForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy);
}
