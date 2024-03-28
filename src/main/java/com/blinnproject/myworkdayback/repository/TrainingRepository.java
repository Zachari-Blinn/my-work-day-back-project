package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.enums.ESessionStatus;
import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.payload.response.TrainingCalendarInfoResponse;
import com.blinnproject.myworkdayback.payload.response.TrainingSessionInfoResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<WorkoutSession, Long> {

  List<WorkoutSession> findAllByCreatedBy(Long createdId);

  Optional<WorkoutSession> findByIdAndCreatedBy(Long id, Long createdId);

  Optional<WorkoutSession> findByName(String name);

  boolean existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(Long parentId, Date performedDate, ESessionStatus trainingStatus, Long createdId);

  Optional<WorkoutSession> findByParentIdAndPerformedDateAndCreatedBy(Long parentId, Date performedDate, Long createdId);

  boolean existsByParentIdAndPerformedDateAndCreatedBy(Long parentId, Date performedDate, Long createdId);

  void deleteByParentIdAndCreatedByAndPerformedDate(Long parentId, Long createdId, Date performedDate);

  @Query("""
      SELECT new com.blinnproject.myworkdayback.payload.response.TrainingCalendarInfoResponse(
          training.id,
          training.name,
          training.iconHexadecimalColor,
          training.startDate,
          training.endDate,
          training.trainingDays
      )
      FROM WorkoutSession training
      WHERE training.createdBy = :createdId
      AND training.parent.id IS NULL
  """)
  List<TrainingCalendarInfoResponse> findAllByCreatedByAndParentIdIsNull(Long createdId);

  @Query(value = """
    SELECT
      CAST(generated_date AS DATE) AS date,
      jsonb_agg(
          jsonb_build_object(
              'trainingName', training.name,
              'trainingColor', training.icon_hexadecimal_color
          )
      ) AS trainings
    FROM
      (SELECT generate_series(
              CAST(:startDate AS timestamp),
              CAST(:endDate AS timestamp),
              interval '1 day'
      ) AS generated_date) AS generated_dates
    JOIN training ON (
      training.created_by = :currentUserId
    AND training.parent_id IS NULL
    AND (
        CAST(generated_date AS date) BETWEEN COALESCE(training.start_date, CAST(:startDate AS date))
        AND COALESCE(training.end_date, CAST(:endDate AS date))
        OR (training.start_date IS NULL AND training.end_date IS NULL)
    )
    AND EXTRACT(ISODOW FROM generated_date) IN (SELECT unnest(training.training_days))
    )
    GROUP BY generated_date
    ORDER BY generated_date
  """,
  nativeQuery = true)
  List<Object[]> getTrainingCalendarData(
      @NotNull Long currentUserId,
      @NotNull Date startDate,
      @NotNull Date endDate
  );

  @Query("""
    SELECT new com.blinnproject.myworkdayback.payload.response.TrainingSessionInfoResponse(
      training.performedDate,
      training.name,
      exercise.name,
      series.repsCount,
      series.weight,
      series.restTime
    )
    FROM TrainingExercises trainingExercises
    JOIN trainingExercises.training training
    JOIN trainingExercises.exercise exercise
    JOIN trainingExercises.seriesList series
    WHERE training.createdBy = :currentUserId
    AND training.parent.id IS NOT NULL
    AND training.performedDate BETWEEN :startDate AND :endDate
    ORDER BY training.performedDate ASC, series.positionIndex ASC
  """)
  List<TrainingSessionInfoResponse> findAllTrainingSession(Long currentUserId, Date startDate, Date endDate);

  @Query("""
    SELECT CASE WHEN EXISTS (
      SELECT training
      FROM WorkoutSession training
      WHERE training.id = :trainingParentId
      AND training.parent = NULL
      AND training.createdBy = :createdBy
    ) THEN true ELSE false END
  """)
  boolean trainingSessionTemplateExists(Long trainingParentId, Long createdBy);
}
