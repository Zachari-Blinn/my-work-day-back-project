package com.blinnproject.myworkdayback.service.workout_session;

import com.blinnproject.myworkdayback.exception.ResourceNotFoundException;
import com.blinnproject.myworkdayback.model.entity.WorkoutExercise;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.enums.EPerformStatus;
import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import com.blinnproject.myworkdayback.repository.WorkoutSessionRepository;
import com.blinnproject.myworkdayback.service.workout_model.WorkoutModelService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

  private final WorkoutSessionRepository workoutSessionRepository;
  private final WorkoutModelService workoutModelService;

  public WorkoutSessionServiceImpl(WorkoutSessionRepository workoutSessionRepository, WorkoutModelService workoutModelService) {
    this.workoutSessionRepository = workoutSessionRepository;
    this.workoutModelService = workoutModelService;
  }

  @Override
  public List<WorkoutSession> findAllByDate(LocalDate startedAt, Long createdBy) {
    int dayOfWeek = startedAt.getDayOfWeek().getValue();
    Timestamp currentDate = Timestamp.valueOf(startedAt.atStartOfDay());
    return workoutSessionRepository.findAllSessionByDate(dayOfWeek, currentDate, createdBy);
  }

  @Override
  public WorkoutSession createWorkoutSession(LocalDateTime startedAt, Long workoutModelId, Long createdBy) {
    // check if workout model exists with user id and get it
    WorkoutModel workoutModel = workoutModelService.findById(workoutModelId, createdBy).orElseThrow(ResourceNotFoundException::new);

    // clone workout model and workout exercises associated with it
    WorkoutSession workoutSession = new WorkoutSession();
    workoutSession.setName("Session " + workoutModel.getName());
    workoutSession.setWorkoutModel(workoutModel);
    workoutSession.setSessionStatus(ESessionStatus.IN_PROGRESS);
    workoutSession.setStartedAt(Timestamp.valueOf(startedAt));

    workoutSession.setWorkoutExerciseList(new ArrayList<>(workoutModel.getWorkoutExerciseList())); // Cloning the list


    workoutSession.setCreatedBy(createdBy);
    workoutSession.setEndedAt(null);

    return workoutSessionRepository.save(workoutSession);
  }
}
