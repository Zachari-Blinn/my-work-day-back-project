package com.blinnproject.myworkdayback.service.schedule;

import com.blinnproject.myworkdayback.model.dto.WorkoutSessionsScheduleDTO;
import com.blinnproject.myworkdayback.model.entity.Schedule;
import com.blinnproject.myworkdayback.model.projection.CombinedWorkoutInfoView;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleView;
import com.blinnproject.myworkdayback.repository.ScheduleRepository;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ScheduleServiceImpl implements ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final I18nService i18n;

  public ScheduleServiceImpl(ScheduleRepository scheduleRepository, I18nService i18n) {
    this.scheduleRepository = scheduleRepository;
    this.i18n = i18n;
  }

  @Override
  public Schedule update(Long scheduleId, Schedule schedule, Long createdBy) {
    return scheduleRepository.findByIdAndCreatedBy(scheduleId, createdBy)
        .map(existingSchedule -> {
          schedule.setId(existingSchedule.getId());
          schedule.setCreatedBy(createdBy);
          return scheduleRepository.save(schedule);
        })
        .orElseThrow(() -> new IllegalArgumentException(i18n.translate("validation.error.schedule-not-found")));
  }

  @Override
  public void delete(Long scheduleId, Long createdBy) {
    scheduleRepository.findByIdAndCreatedBy(scheduleId, createdBy)
        .ifPresent(scheduleRepository::delete);
  }

  @Override
  public List<WorkoutScheduleView> getWorkoutScheduleForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy) {
    validateDates(startDate, endDate);

    return scheduleRepository.findAllModelsSessionsByDateRange(startDate, endDate, createdBy);
  }

  @Override
  public List<WorkoutSessionsScheduleDTO> getWorkoutSessionsForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy) {
    validateDates(startDate, endDate);

    List<CombinedWorkoutInfoView> combinedWorkoutInfoViewList = scheduleRepository.findAllSessionsAndModelsByDateRange(startDate, endDate, createdBy);

    Map<LocalDate, WorkoutSessionsScheduleDTO> workoutSessionsMap = new HashMap<>();

    for (CombinedWorkoutInfoView workoutInfo : combinedWorkoutInfoViewList) {
      LocalDate date = workoutInfo.getDate();

      // Retrieve or create a new WorkoutSessionsScheduleDTO for the current date
      WorkoutSessionsScheduleDTO workoutSessionsScheduleDTO = workoutSessionsMap.getOrDefault(date, new WorkoutSessionsScheduleDTO());
      workoutSessionsScheduleDTO.setDate(date);

      // Depending on the workout type, set the appropriate fields
      if ("MODEL".equals(workoutInfo.getWorkoutType())) {
        workoutSessionsScheduleDTO.setWorkoutModelId(workoutInfo.getWorkoutModelId());
        workoutSessionsScheduleDTO.setWorkoutModelName(workoutInfo.getWorkoutModelName());
        workoutSessionsScheduleDTO.setWorkoutModelIconName(workoutInfo.getWorkoutModelIconName());
        workoutSessionsScheduleDTO.setWorkoutModelIconHexadecimalColor(workoutInfo.getWorkoutModelIconHexadecimalColor());
        workoutSessionsScheduleDTO.setStartTime(workoutInfo.getStartTime());
        workoutSessionsScheduleDTO.setEndTime(workoutInfo.getEndTime());
        workoutSessionsScheduleDTO.setSessionStatus(workoutInfo.getSessionStatus());
      } else if ("SESSION".equals(workoutInfo.getWorkoutType())) {
        // Create a new WorkoutSessionListDTO
        WorkoutSessionsScheduleDTO.WorkoutSessionListDTO session = new WorkoutSessionsScheduleDTO.WorkoutSessionListDTO();
        session.setWorkoutSessionId(workoutInfo.getWorkoutSessionId());
        session.setStatus(workoutInfo.getSessionStatus());
        session.setStartTime(workoutInfo.getStartTime());
        session.setEndTime(workoutInfo.getEndTime());

        // Retrieve or create the list of workout sessions for the current date
        ArrayList<WorkoutSessionsScheduleDTO.WorkoutSessionListDTO> workoutSessionList = workoutSessionsScheduleDTO.getWorkoutSessionList();
        if (workoutSessionList == null) {
          workoutSessionList = new ArrayList<>();
        }
        workoutSessionList.add(session);
        workoutSessionsScheduleDTO.setWorkoutSessionList(workoutSessionList);
      }

      // Update the map with the modified WorkoutSessionsScheduleDTO
      workoutSessionsMap.put(date, workoutSessionsScheduleDTO);
    }

    // Sort the values of the map by date
    List<WorkoutSessionsScheduleDTO> sortedWorkoutSessions = new ArrayList<>(workoutSessionsMap.values());
    sortedWorkoutSessions.sort(Comparator.comparing(WorkoutSessionsScheduleDTO::getDate));

    return sortedWorkoutSessions;
  }

  /**
   * PRIVATE METHODS
   */

  private void validateDates(LocalDate startDate, LocalDate endDate) {
    validateStartDateBeforeEndDate(startDate, endDate);
    validatePeriodLimit(startDate, endDate);
  }

  private void validateStartDateBeforeEndDate(LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException(i18n.translate("validation.error.start-date-after-end-date"));
    }
  }

  private void validatePeriodLimit(LocalDate startDate, LocalDate endDate) {
    if (startDate.plusYears(2).plusDays(1).isBefore(endDate)) {
      throw new IllegalArgumentException(i18n.translate("validation.error.period-exceeds-limit"));
    }
  }
}
