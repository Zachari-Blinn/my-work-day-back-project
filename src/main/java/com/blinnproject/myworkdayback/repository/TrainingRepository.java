package com.blinnproject.myworkdayback.repository;

import com.blinnproject.myworkdayback.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

  List<Training> findAllByCreatedBy(Long createdId);

  boolean existsByIdAndCreatedBy(Long id, Long createdId);

  Optional<Training> findByIdAndCreatedBy(Long id, Long createdId);
}
