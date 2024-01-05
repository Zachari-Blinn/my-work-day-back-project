package com.blinnproject.myworkdayback.model;

import lombok.Getter;

@Getter
public enum ETrainingStatus {
  CANCELLED(0),
  IN_PROGRESS(1),
  PERFORMED(2);

  private final int value;

  ETrainingStatus(int value) {
    this.value = value;
  }

  public static ETrainingStatus of(int u) {
    return switch (u) {
      case 0 -> CANCELLED;
      case 1 -> IN_PROGRESS;
      case 2 -> PERFORMED;
      default -> throw new IllegalArgumentException("Unknown training status: " + u);
    };
  }
}
