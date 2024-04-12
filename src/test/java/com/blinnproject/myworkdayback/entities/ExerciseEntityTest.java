package com.blinnproject.myworkdayback.entities;

import com.blinnproject.myworkdayback.model.entity.Exercise;
import com.blinnproject.myworkdayback.model.enums.EMuscle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ExerciseEntityTest {

  @Test
  void testCreateExercise() {
    Exercise exercise = new Exercise();

    exercise.setName("Bench Press");
    exercise.setMusclesUsed(Collections.singleton(EMuscle.ABDOMINALS));

    Assertions.assertEquals("Bench Press", exercise.getName());
    Assertions.assertEquals(Collections.singleton(EMuscle.ABDOMINALS), exercise.getMusclesUsed());
  }
}
