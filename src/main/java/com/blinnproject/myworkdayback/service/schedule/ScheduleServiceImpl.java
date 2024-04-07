package com.blinnproject.myworkdayback.service.schedule;

import com.blinnproject.myworkdayback.model.projection.CombinedWorkoutInfoDTO;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleDTO;
import com.blinnproject.myworkdayback.repository.ScheduleRepository;
import com.blinnproject.myworkdayback.service.i18n.I18nService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
  private final ScheduleRepository scheduleRepository;
  private final I18nService i18n;

  public ScheduleServiceImpl(ScheduleRepository scheduleRepository, I18nService i18n) {
    this.scheduleRepository = scheduleRepository;
    this.i18n = i18n;
  }

  @Override
  public void delete(Long scheduleId, Long createdBy) {
    scheduleRepository.findByIdAndCreatedBy(scheduleId, createdBy)
        .ifPresent(scheduleRepository::delete);
  }

  @Override
  public List<WorkoutScheduleDTO> getWorkoutScheduleForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy) {
    validateDates(startDate, endDate);

    return scheduleRepository.findAllModelsSessionsByDateRange(startDate, endDate, createdBy);
  }

  @Override
  public List<CombinedWorkoutInfoDTO> getWorkoutSessionsForPeriod(LocalDate startDate, LocalDate endDate, Long createdBy) {
    validateDates(startDate, endDate);

    return scheduleRepository.findAllSessionsAndModelsByDateRange(startDate, endDate, createdBy);
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
