package com.blinnproject.myworkdayback.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingSessionInfoResponse {
  Date trainingDate;
  String trainingName;
  String exerciseName;
  int repsCount;
  int weight;
  String restTime;
}
