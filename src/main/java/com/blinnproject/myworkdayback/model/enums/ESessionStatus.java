package com.blinnproject.myworkdayback.model.enums;

import lombok.Getter;

@Getter
public enum ESessionStatus {
  CANCELLED(0),
  IN_PROGRESS(1),
  PERFORMED(2);

  private final int value;

  ESessionStatus(int value) {
    this.value = value;
  }

  public static ESessionStatus of(int u) {
    return switch (u) {
      case 0 -> CANCELLED;
      case 1 -> IN_PROGRESS;
      case 2 -> PERFORMED;
      default -> throw new IllegalArgumentException("Unknown training status: " + u);
    };
  }
}
