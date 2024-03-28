package com.blinnproject.myworkdayback.payload.dto.exercise;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.enums.EMuscle;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link Exercise}
 */
@Value
public class ExerciseDto implements Serializable {
  Long id;
  Long createdBy;
  Long lastUpdatedBy;
  Instant createdOn;
  Instant lastUpdatedOn;
  Boolean isActive;
  String name;
  Set<EMuscle> musclesUsed;
}