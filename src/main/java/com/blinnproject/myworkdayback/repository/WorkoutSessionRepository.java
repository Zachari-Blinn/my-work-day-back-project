package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.entity.WorkoutSession;
import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

  @Query("""
      SELECT workoutSession
      FROM WorkoutSession workoutSession
      JOIN workoutSession.workoutModel workoutModel
      JOIN workoutModel.schedules schedule
      WHERE
        CASE
          WHEN :dayOfWeek = 1 AND schedule.sunday = TRUE THEN TRUE
          WHEN :dayOfWeek = 2 AND schedule.monday = TRUE THEN TRUE
          WHEN :dayOfWeek = 3 AND schedule.tuesday = TRUE THEN TRUE
          WHEN :dayOfWeek = 4 AND schedule.wednesday = TRUE THEN TRUE
          WHEN :dayOfWeek = 5 AND schedule.thursday = TRUE THEN TRUE
          WHEN :dayOfWeek = 6 AND schedule.friday = TRUE THEN TRUE
          WHEN :dayOfWeek = 7 AND schedule.saturday = TRUE THEN TRUE
          ELSE FALSE
        END = TRUE
      AND workoutSession.createdBy = :createdBy
      AND workoutSession.startedAt >= :currentDate
      AND workoutSession.endedAt <= :currentDate
  """)
  List<WorkoutSession> findAllSessionByDate(
      @Param("dayOfWeek") int dayOfWeek,
      @Param("currentDate") Timestamp currentDate,
      @Param("createdBy") Long createdBy
  );
}
