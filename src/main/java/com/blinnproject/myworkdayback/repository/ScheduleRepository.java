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
      d::date AS date,
      workout_model.id AS workout_model_id,
      workout_model.name AS workout_model_name,
      workout_model.icon_name AS workout_model_icon_name,
      workout_model.icon_hexadecimal_color AS workout_model_icon_hexadecimal_color,
      schedule.start_time AS start_time,
      schedule.end_time AS end_time
    FROM
      generate_series(:startDate::timestamp, :endDate::timestamp, '1 day') AS d
    LEFT JOIN
      schedule ON d::date >= schedule.start_date
               AND (d::date <= schedule.end_date OR schedule.end_date IS NULL)
               AND (
                  (EXTRACT(ISODOW FROM d::date) = 1 AND schedule.monday) OR
                  (EXTRACT(ISODOW FROM d::date) = 2 AND schedule.tuesday) OR
                  (EXTRACT(ISODOW FROM d::date) = 3 AND schedule.wednesday) OR
                  (EXTRACT(ISODOW FROM d::date) = 4 AND schedule.thursday) OR
                  (EXTRACT(ISODOW FROM d::date) = 5 AND schedule.friday) OR
                  (EXTRACT(ISODOW FROM d::date) = 6 AND schedule.saturday) OR
                  (EXTRACT(ISODOW FROM d::date) = 7 AND schedule.sunday)
              )
    LEFT JOIN
      workout workout_model ON schedule.workout_model_id = workout_model.id
    WHERE
      workout_model.is_active = true
      AND workout_model.created_by = :createdBy
    ORDER BY
      d::date, workout_model.name
  """, nativeQuery = true)
  List<WorkoutScheduleDTO> findAllModelsSessionsByDateRange(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("createdBy") Long createdBy
  );

  @Query(value = """
    SELECT * FROM (
        SELECT
            generated_date::date AS date,
            'MODEL' AS workout_type,
            workout_model.id AS workout_model_id,
            NULL AS workout_session_id,
            workout_model.name AS workout_model_name,
            workout_model.icon_name AS workout_model_icon_name,
            workout_model.icon_hexadecimal_color AS workout_model_icon_hexadecimal_color,
            schedule.start_time AS start_time,
            schedule.end_time AS end_time,
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
                    AND workout_session.started_at::date = generated_date::date
            ) AS session_status
        FROM
            generate_series(:startDate::timestamp, :endDate::timestamp, '1 day') AS generated_date
        JOIN
            schedule schedule ON generated_date::date >= schedule.start_date
                     AND (generated_date::date <= schedule.end_date OR schedule.end_date IS NULL)
                     AND (
                        (EXTRACT(ISODOW FROM generated_date::date) = 1 AND schedule.monday) OR
                        (EXTRACT(ISODOW FROM generated_date::date) = 2 AND schedule.tuesday) OR
                        (EXTRACT(ISODOW FROM generated_date::date) = 3 AND schedule.wednesday) OR
                        (EXTRACT(ISODOW FROM generated_date::date) = 4 AND schedule.thursday) OR
                        (EXTRACT(ISODOW FROM generated_date::date) = 5 AND schedule.friday) OR
                        (EXTRACT(ISODOW FROM generated_date::date) = 6 AND schedule.saturday) OR
                        (EXTRACT(ISODOW FROM generated_date::date) = 7 AND schedule.sunday)
                    )
        LEFT JOIN
            workout workout_model ON schedule.workout_model_id = workout_model.id
              AND workout_model.is_active = true
              AND workout_model.created_by = :createdBy
              AND workout_model.workout_type = 'MODEL'

        UNION
        
        SELECT
            workout_session.started_at::date AS date,
            'SESSION' AS workout_type,
            workout_session.workout_model_id AS workout_model_id,
            workout_session.id AS workout_session_id,
            workout_model.name AS workout_model_name,
            workout_model.icon_name AS workout_model_icon_name,
            workout_model.icon_hexadecimal_color AS workout_model_icon_hexadecimal_color,
            workout_session.started_at::time AS start_time,
            workout_session.ended_at::time AS end_time,
            workout_session.session_status AS session_status
        FROM
            workout workout_session
        JOIN
            workout workout_model ON workout_session.workout_model_id = workout_model.id
        WHERE
            workout_session.is_active = true
            AND workout_session.created_by = :createdBy
            AND workout_session.workout_type = 'SESSION'
            AND workout_session.started_at::date BETWEEN :startDate AND :endDate
    ) AS combined_results
    ORDER BY
      date, workout_model_name
  """, nativeQuery = true)
  List<CombinedWorkoutInfoDTO> findAllSessionsAndModelsByDateRange(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("createdBy") Long createdBy
  );
}
