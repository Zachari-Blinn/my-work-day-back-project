package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.Schedule;
import com.blinnproject.myworkdayback.model.projection.CombinedWorkoutInfoDTO;
import com.blinnproject.myworkdayback.model.projection.WorkoutScheduleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  Optional<Schedule> findByIdAndCreatedBy(Long scheduleId, Long createdBy);

  @Query(value = """
    SELECT
      CAST(d AS date) AS rawDate,
      workout_model.id AS workoutModelId,
      workout_model.name AS workoutModelName,
      workout_model.icon_name AS workoutModelIconName,
      workout_model.icon_hexadecimal_color AS workoutModelIconHexadecimalColor,
      schedule.start_time AS rawStartTime,
      schedule.end_time AS rawEndTime
    FROM
      generate_series(CAST(:startDate AS timestamp), CAST(:endDate AS timestamp), interval '1 day') AS d
    LEFT JOIN
      schedule ON CAST(d AS date) >= schedule.start_date
               AND (CAST(d AS date) <= schedule.end_date OR schedule.end_date IS NULL)
               AND (
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 1 AND schedule.monday) OR
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 2 AND schedule.tuesday) OR
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 3 AND schedule.wednesday) OR
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 4 AND schedule.thursday) OR
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 5 AND schedule.friday) OR
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 6 AND schedule.saturday) OR
                  (EXTRACT(ISODOW FROM CAST(d AS date)) = 7 AND schedule.sunday)
              )
    LEFT JOIN
      workout workout_model ON schedule.workout_model_id = workout_model.id
    WHERE
      workout_model.is_active = true
      AND workout_model.created_by = :createdBy
    ORDER BY
      CAST(d AS date), workout_model.name
  """, nativeQuery = true)
  List<WorkoutScheduleDTO> findAllModelsSessionsByDateRange(
      LocalDate startDate,
      LocalDate endDate,
      Long createdBy
  );

  @Query(value = """
    SELECT * FROM (
        SELECT
            CAST(generated_date AS date) AS date,
            'MODEL' AS workoutType,
            workout_model.id AS workoutModelId,
            NULL AS workoutSessionId,
            workout_model.name AS workoutModelName,
            workout_model.icon_name AS workoutModelIconName,
            workout_model.icon_hexadecimal_color AS workoutModelIconHexadecimalColor,
            schedule.start_time AS startTime,
            schedule.end_time AS endTime,
            (
                SELECT
                    CASE
                        WHEN COUNT(workout_session.id) = 0 THEN 'NOT_STARTED'
                        WHEN COUNT(workout_session.id) = COUNT(workout_session.id) FILTER (WHERE workout_session.session_status = 'COMPLETED') THEN 'COMPLETED'
                        ELSE 'IN_PROGRESS'
                    END
                FROM
                    workout workout_session JOIN workout workout_model ON workout_session.workout_model_id = workout_model.id
                WHERE
                    workout_session.is_active = true
                    AND workout_session.created_by = :createdBy
                    AND workout_session.workout_type = 'SESSION'
                    AND CAST(workout_session.started_at AS date) = CAST(generated_date AS date)
            ) AS sessionStatus
        FROM
            generate_series(CAST(:startDate AS timestamp), CAST(:endDate AS timestamp), interval '1 day') AS generated_date
        JOIN
            schedule schedule ON CAST(generated_date AS date) >= schedule.start_date
                     AND (CAST(generated_date AS date) <= schedule.end_date OR schedule.end_date IS NULL)
                     AND (
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 1 AND schedule.monday) OR
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 2 AND schedule.tuesday) OR
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 3 AND schedule.wednesday) OR
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 4 AND schedule.thursday) OR
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 5 AND schedule.friday) OR
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 6 AND schedule.saturday) OR
                        (EXTRACT(ISODOW FROM CAST(generated_date AS date)) = 7 AND schedule.sunday)
                    )
        LEFT JOIN
            workout workout_model ON schedule.workout_model_id = workout_model.id
              AND workout_model.is_active = true
              AND workout_model.created_by = :createdBy
              AND workout_model.workout_type = 'MODEL'

        UNION
        
        SELECT
            CAST(workout_session.started_at AS date) AS date,
            'SESSION' AS workoutType,
            workout_session.workout_model_id AS workoutModelId,
            workout_session.id AS workoutSessionId,
            workout_model.name AS workoutModelName,
            workout_model.icon_name AS workoutModelIconName,
            workout_model.icon_hexadecimal_color AS workoutModelIconHexadecimalColor,
            CAST(workout_session.started_at AS time) AS startTime,
            CAST(workout_session.ended_at AS time) AS endTime,
            workout_session.session_status AS sessionStatus
        FROM
            workout workout_session
        JOIN
            workout workout_model ON workout_session.workout_model_id = workout_model.id
        WHERE
            workout_session.is_active = true
            AND workout_session.created_by = :createdBy
            AND workout_session.workout_type = 'SESSION'
            AND CAST(workout_session.started_at AS date) BETWEEN :startDate AND :endDate
    ) AS combined_results
    ORDER BY
      date, workoutModelName
  """, nativeQuery = true)
  List<CombinedWorkoutInfoDTO> findAllSessionsAndModelsByDateRange(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("createdBy") Long createdBy
  );
}
