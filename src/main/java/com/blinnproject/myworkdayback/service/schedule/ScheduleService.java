package com.blinnproject.myworkdayback.service.schedule;

import com.blinnproject.myworkdayback.model.projection.CombinedWorkoutInfoDTO;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
  void delete(Long scheduleId, Long createdBy);

  List<WorkoutScheduleDTO> getWorkoutScheduleForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy);

  List<CombinedWorkoutInfoDTO> getWorkoutSessionsForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy);
}
