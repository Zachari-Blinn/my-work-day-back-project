package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.ETrainingStatus;
import com.blinnproject.myworkdayback.model.Training;
import com.blinnproject.myworkdayback.payload.response.TrainingCalendarInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

  List<Training> findAllByCreatedBy(Long createdId);

  boolean existsByIdAndCreatedBy(Long id, Long createdId);

  Optional<Training> findByIdAndCreatedBy(Long id, Long createdId);

  Optional<Training> findByName(String name);

  boolean existsByParentIdAndPerformedDateAndTrainingStatusAndCreatedBy(Long parentId, Date performedDate, ETrainingStatus trainingStatus, Long createdId);

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
      FROM Training training
      WHERE training.createdBy = :createdId
      AND training.parent.id IS NULL
  """)
  List<TrainingCalendarInfoResponse> findAllByCreatedByAndParentIdIsNull(Long createdId);
}
