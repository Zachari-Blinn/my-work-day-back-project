package com.blinnproject.myworkdayback.payload.dto.series;

import com.blinnproject.myworkdayback.model.EDifficulty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.blinnproject.myworkdayback.model.Series}
 */
@Value
public class SeriesDto implements Serializable {
  Long id;
  Long createdBy;
  Long lastUpdatedBy;
  Instant createdOn;
  Instant lastUpdatedOn;
  Boolean isActive;
  @Min(0)
  int positionIndex;
  SeriesDto parent;
  @Min(0)
  @Max(150)
  int weight;
  String restTime;
  String notes;
  @Min(0)
  @Max(100)
  int repsCount;
  EDifficulty difficulty;
}