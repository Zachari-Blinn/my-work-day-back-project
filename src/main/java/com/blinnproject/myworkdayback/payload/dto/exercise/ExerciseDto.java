package com.blinnproject.myworkdayback.payload.dto.exercise;

import com.blinnproject.myworkdayback.model.EMuscle;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * DTO for {@link com.blinnproject.myworkdayback.model.Exercise}
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