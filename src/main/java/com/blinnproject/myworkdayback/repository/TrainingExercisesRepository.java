package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.TrainingExercises;
import com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingExercisesRepository extends JpaRepository<TrainingExercises, Long> {
    @Query("SELECT te FROM TrainingExercises te WHERE te.training.id = :trainingId AND te.parent = NULL AND te.training.createdBy = :createdBy")
    List<TrainingExercises> findTemplateByTrainingIdAndCreatedBy(Long trainingId, Long createdBy);

    List<TrainingExercises> findByTrainingIdAndTrainingCreatedBy(Long trainingId, Long createdBy);

//    for checking if training exercises series is completed or not, compare trainingExercises series with parent_id and training_day is null (it is template), with trainingExercises series with parent_id and training_day is not null (it's training session of the day)
    @Query("""
            SELECT new com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo(
                trainingExercisesTemplate.training.id,
                trainingExercisesTemplate.training.name,
                (
                    SELECT trainingExercises.training.trainingStatus
                    FROM TrainingExercises trainingExercises
                    WHERE trainingExercises.parent.id IS NOT NULL
                    AND trainingExercises.trainingDay IS NOT NULL
                    AND trainingExercises.parent.id = trainingExercisesTemplate.id
                    AND trainingExercises.trainingDay = :selectedTrainingDay
                ),
                trainingExercisesTemplate.training.trainingDays,
                trainingExercisesTemplate.training.iconName,
                trainingExercisesTemplate.training.iconHexadecimalColor,
                trainingExercisesTemplate.exercise.id,
                trainingExercisesTemplate.exercise.name,
                seriesTemplate.id,
                seriesTemplate.positionIndex,
                seriesTemplate.repsCount,
                seriesTemplate.restTime,
                seriesTemplate.weight,
                CASE WHEN EXISTS (
                    SELECT series.id
                    FROM TrainingExercises trainingExercises
                    JOIN trainingExercises.seriesList series
                    WHERE trainingExercises.parent.id IS NOT NULL
                    AND trainingExercises.trainingDay IS NOT NULL
                    AND trainingExercises.parent.id = trainingExercisesTemplate.id
                    AND series.parent.id = seriesTemplate.id
                    AND trainingExercises.trainingDay = :selectedTrainingDay
                ) THEN true ELSE false END
            )
            FROM TrainingExercises trainingExercisesTemplate
            JOIN trainingExercisesTemplate.seriesList seriesTemplate
            WHERE trainingExercisesTemplate.trainingDay IS NULL
            AND trainingExercisesTemplate.training.createdBy = :currentUser
            AND trainingExercisesTemplate.parent IS NULL
            ORDER BY trainingExercisesTemplate.exercise.id, seriesTemplate.positionIndex
    """)
    List<TrainingExercisesSeriesInfo> getAllTrainingsSeriesStatusByDate(Long currentUser, Date selectedTrainingDay);

    @Query("""
            SELECT new com.blinnproject.myworkdayback.payload.response.TrainingExercisesSeriesInfo(
                trainingExercisesTemplate.training.id,
                trainingExercisesTemplate.training.name,
                (
                    SELECT trainingExercises.training.trainingStatus
                    FROM TrainingExercises trainingExercises
                    WHERE trainingExercises.parent.id IS NOT NULL
                    AND trainingExercises.trainingDay IS NOT NULL
                    AND trainingExercises.parent.id = trainingExercisesTemplate.id
                    AND trainingExercises.trainingDay = :selectedTrainingDay
                ),
                trainingExercisesTemplate.training.trainingDays,
                trainingExercisesTemplate.training.iconName,
                trainingExercisesTemplate.training.iconHexadecimalColor,
                trainingExercisesTemplate.exercise.id,
                trainingExercisesTemplate.exercise.name,
                seriesTemplate.id,
                seriesTemplate.positionIndex,
                seriesTemplate.repsCount,
                seriesTemplate.restTime,
                seriesTemplate.weight,
                CASE WHEN EXISTS (
                    SELECT series.id
                    FROM TrainingExercises trainingExercises
                    JOIN trainingExercises.seriesList series
                    WHERE trainingExercises.parent.id IS NOT NULL
                    AND trainingExercises.trainingDay IS NOT NULL
                    AND trainingExercises.parent.id = trainingExercisesTemplate.id
                    AND series.parent.id = seriesTemplate.id
                    AND trainingExercises.trainingDay = :selectedTrainingDay
                ) THEN true ELSE false END
            )
            FROM TrainingExercises trainingExercisesTemplate
            JOIN trainingExercisesTemplate.seriesList seriesTemplate
            WHERE trainingExercisesTemplate.training.id = :trainingId
            AND trainingExercisesTemplate.training.createdBy = :currentUser
            AND trainingExercisesTemplate.trainingDay IS NULL
            AND trainingExercisesTemplate.parent IS NULL
            ORDER BY trainingExercisesTemplate.exercise.id, seriesTemplate.positionIndex
    """)
    List<TrainingExercisesSeriesInfo> getTrainingSeriesStatusByDate(Long currentUser, Long trainingId, Date selectedTrainingDay);
}
//todo ajouter parent_id a series également