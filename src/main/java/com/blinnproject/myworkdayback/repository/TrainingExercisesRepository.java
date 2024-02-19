package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.projection.TrainingExercisesSeriesInfoProjection;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface TrainingExercisesRepository extends JpaRepository<TrainingExercises, Long> {
    @Query("""
        SELECT te
        FROM TrainingExercises te
        WHERE te.training.id = :trainingParentId
        AND te.parent = NULL
        AND te.training.createdBy = :createdBy
    """)
    List<TrainingExercises> findTemplateByTrainingIdAndCreatedBy(Long trainingParentId, Long createdBy);

    List<TrainingExercises> findByTrainingIdAndTrainingCreatedBy(Long trainingId, Long createdBy);

    @Query(value = """
        SELECT
            training_template.id AS trainingId,
            training_template.name AS trainingName,
            (
                SELECT training.training_status
                FROM training_exercises AS training_exercises
                JOIN training AS training ON training_exercises.training_id = training.id
                WHERE training_exercises.parent_id IS NOT NULL
                AND training_exercises.training_day IS NOT NULL
                AND training_exercises.parent_id = training_exercises_template.id
                AND training_exercises.training_day = :selectedTrainingDay
            ) AS rawTrainingStatus,
            training_template.training_days AS rawTrainingDays,
            training_template.icon_name AS trainingIconName,
            training_template.icon_hexadecimal_color AS trainingIconHexadecimalColor,
            exercise_template.id AS exerciseId,
            exercise_template.name AS exerciseName,
            series_template.id AS seriesId,
            series_template.position_index AS seriesPositionIndex,
            series_template.reps_count AS seriesRepsCount,
            series_template.rest_time AS seriesRestTime,
            series_template.weight AS seriesWeight,
            CASE WHEN EXISTS (
                SELECT series.id
                FROM training_exercises AS training_exercises
                JOIN series ON training_exercises.id = series.training_exercises_id
                WHERE training_exercises.parent_id IS NOT NULL
                AND training_exercises.training_day IS NOT NULL
                AND training_exercises.parent_id = training_exercises_template.id
                AND series.parent_id = series_template.id
                AND training_exercises.training_day = CAST(:selectedTrainingDay AS DATE)
            ) THEN true ELSE false END AS isCompleted
        FROM training_exercises AS training_exercises_template
        JOIN training AS training_template ON training_exercises_template.training_id = training_template.id
        JOIN exercise AS exercise_template ON training_exercises_template.exercise_id = exercise_template.id
        JOIN series AS series_template ON training_exercises_template.id = series_template.training_exercises_id
        WHERE training_exercises_template.training_day IS NULL
        AND training_template.created_by = :currentUser
        AND training_exercises_template.parent_id IS NULL
        AND (training_template.start_date IS NULL OR training_template.start_date <= CAST(:selectedTrainingDay AS DATE))
        AND (training_template.end_date IS NULL OR training_template.end_date >= CAST(:selectedTrainingDay AS DATE))
        AND EXTRACT(ISODOW FROM CAST(:selectedTrainingDay AS timestamp)) IN (SELECT unnest(training_template.training_days))
    """,
    nativeQuery = true)
    List<TrainingExercisesSeriesInfoProjection> getAllTrainingsSeriesStatusByDate(
        @NotNull Long currentUser,
        @NotNull Date selectedTrainingDay
    );
}
