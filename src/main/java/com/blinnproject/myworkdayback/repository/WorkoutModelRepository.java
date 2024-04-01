package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.dto.WorkoutModelSessionDateDTO;
import com.blinnproject.myworkdayback.model.entity.WorkoutModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutModelRepository extends JpaRepository<WorkoutModel, Long> {
  Optional<WorkoutModel> findByName(String name);

  Optional<WorkoutModel> findByIdAndCreatedBy(Long id, Long createdBy);

  ArrayList<WorkoutModel> findAllByCreatedBy(Long createdBy);

  @Query(value = """
    SELECT
      d::date AS date,
      wm.id AS workout_model_id,
      wm.name AS workout_model_name,
      wm.icon_name AS workout_model_icon_name,
      wm.icon_hexadecimal_color AS workout_model_icon_hexadecimal_color,
      s.start_time AS start_time,
      s.end_time AS end_time
    FROM
      generate_series(:startDate::timestamp, :endDate::timestamp, '1 day') AS d
    LEFT JOIN
      schedule s ON d::date >= s.start_date\s
               AND (d::date <= s.end_date OR s.end_date IS NULL)
               AND (
                  (EXTRACT(ISODOW FROM d::date) = 1 AND s.monday) OR
                  (EXTRACT(ISODOW FROM d::date) = 2 AND s.tuesday) OR
                  (EXTRACT(ISODOW FROM d::date) = 3 AND s.wednesday) OR
                  (EXTRACT(ISODOW FROM d::date) = 4 AND s.thursday) OR
                  (EXTRACT(ISODOW FROM d::date) = 5 AND s.friday) OR
                  (EXTRACT(ISODOW FROM d::date) = 6 AND s.saturday) OR
                  (EXTRACT(ISODOW FROM d::date) = 7 AND s.sunday)
              )
    LEFT JOIN
      workout wm ON s.workout_model_id = wm.id
    WHERE
      wm.is_active = true
      AND wm.created_by = :createdBy
    ORDER BY
      d::date, wm.name
  """, nativeQuery = true)
  List<WorkoutModelSessionDateDTO> findAllWorkoutModelSessionBetweenDates(
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("createdBy") Long createdBy
  );
}