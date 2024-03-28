package com.blinnproject.myworkdayback.model.enums;

import lombok.Getter;

@Getter
public enum EDayOfWeek {
  SUNDAY(0),
  MONDAY(1),
  TUESDAY(2),
  WEDNESDAY(3),
  THURSDAY(4),
  FRIDAY(5),
  SATURDAY(6);

  private final int value;

  EDayOfWeek(int value) {
    this.value = value;
  }

  public static EDayOfWeek of(int u) {
    return switch (u) {
      case 0 -> SUNDAY;
      case 1 -> MONDAY;
      case 2 -> TUESDAY;
      case 3 -> WEDNESDAY;
      case 4 -> THURSDAY;
      case 5 -> FRIDAY;
      case 6 -> SATURDAY;
      default -> throw new IllegalArgumentException("Unknown day of week: " + u);
    };
  }
}
