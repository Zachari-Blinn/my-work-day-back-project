package com.blinnproject.myworkdayback.service.schedule;

import com.blinnproject.myworkdayback.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {
  private final ScheduleRepository scheduleRepository;

  public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
    this.scheduleRepository = scheduleRepository;
  }

  @Override
  public void delete(Long scheduleId, Long createdBy) {
    scheduleRepository.findByIdAndCreatedBy(scheduleId, createdBy)
        .ifPresent(scheduleRepository::delete);
  }
}
