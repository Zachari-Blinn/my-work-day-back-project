package com.blinnproject.myworkdayback.payload.response;

import com.blinnproject.myworkdayback.model.enums.EDayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingCalendarInfoResponse {
  private Long trainingId;
  private String trainingIconName;
  private String trainingIconHexadecimalColor;
  private Date startDate;
  private Date endDate;
  private ArrayList<EDayOfWeek> trainingDays;
}
