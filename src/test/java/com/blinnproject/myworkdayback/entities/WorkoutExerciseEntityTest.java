package com.blinnproject.myworkdayback.entities;

import com.blinnproject.myworkdayback.model.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class WorkoutExerciseEntityTest {

  @Mock
  private WorkoutModel workoutModel;

  @Mock
  private Exercise exercise;

  @InjectMocks
  private WorkoutExercise workoutExercise;

  @BeforeEach
  public void setUp() {
    workoutExercise = new WorkoutExercise();
    workoutExercise.setWorkout(workoutModel);
    workoutExercise.setExercise(exercise);
    workoutExercise.setPositionIndex(1);
    workoutExercise.setNotes("Test notes");
    workoutExercise.setNumberOfWarmUpSets(2);
  }

  @Test
  void testAddWorkoutSets() {
    // Given
    WorkoutSet workoutSet1 = new WorkoutSet();
    workoutSet1.setPositionIndex(1);
    WorkoutSet workoutSet2 = new WorkoutSet();
    workoutSet2.setPositionIndex(2);
    List<WorkoutSet> providedSetsList = new ArrayList<>();
    providedSetsList.add(workoutSet1);
    providedSetsList.add(workoutSet2);

    // When
    workoutExercise.addWorkoutSets(providedSetsList);

    // Then
    Assertions.assertEquals(2, workoutExercise.getWorkoutSets().size());
    Assertions.assertEquals(1, workoutExercise.getWorkoutSets().get(0).getPositionIndex());
    Assertions.assertEquals(2, workoutExercise.getWorkoutSets().get(1).getPositionIndex());
  }


  @Test
  void testBelongsToSession() {
    Assertions.assertFalse(workoutExercise.belongsToSession());
  }

  @Test
  void testBelongsToModel() {
    Assertions.assertTrue(workoutExercise.belongsToModel());
  }

  @Test
  void testGetWorkout() {
    Assertions.assertEquals(workoutModel, workoutExercise.getWorkout());
  }

  @Test
  void testGetExercise() {
    Assertions.assertEquals(exercise, workoutExercise.getExercise());
  }

  @Test
  void testGetPositionIndex() {
    Assertions.assertEquals(1, workoutExercise.getPositionIndex());
  }

  @Test
  void testGetNotes() {
    Assertions.assertEquals("Test notes", workoutExercise.getNotes());
  }

  @Test
  void testGetNumberOfWarmUpSets() {
    Assertions.assertEquals(2, workoutExercise.getNumberOfWarmUpSets());
  }

  @Test
  void testGetWorkoutSets() {
    Assertions.assertEquals(0, workoutExercise.getWorkoutSets().size());
  }

  @Test
  void testSetWorkout() {
    WorkoutModel newWorkoutModel = new WorkoutModel();
    workoutExercise.setWorkout(newWorkoutModel);
    Assertions.assertEquals(newWorkoutModel, workoutExercise.getWorkout());
  }

  @Test
  void testSetExercise() {
    Exercise newExercise = new Exercise();
    workoutExercise.setExercise(newExercise);
    Assertions.assertEquals(newExercise, workoutExercise.getExercise());
  }

  @Test
  void testSetPositionIndex() {
    workoutExercise.setPositionIndex(2);
    Assertions.assertEquals(2, workoutExercise.getPositionIndex());
  }

  @Test
  void testSetNotes() {
    workoutExercise.setNotes("New notes");
    Assertions.assertEquals("New notes", workoutExercise.getNotes());
  }

  @Test
  void testSetNumberOfWarmUpSets() {
    workoutExercise.setNumberOfWarmUpSets(3);
    Assertions.assertEquals(3, workoutExercise.getNumberOfWarmUpSets());
  }

  @Test
  void testSetWorkoutSets() {
    WorkoutSet workoutSet1 = new WorkoutSet();
    workoutSet1.setPositionIndex(1);
    WorkoutSet workoutSet2 = new WorkoutSet();
    workoutSet2.setPositionIndex(2);
    List<WorkoutSet> providedSetsList = new ArrayList<>();
    providedSetsList.add(workoutSet1);
    providedSetsList.add(workoutSet2);
    workoutExercise.setWorkoutSets(providedSetsList);
    Assertions.assertEquals(2, workoutExercise.getWorkoutSets().size());
    Assertions.assertEquals(1, workoutExercise.getWorkoutSets().get(0).getPositionIndex());
    Assertions.assertEquals(2, workoutExercise.getWorkoutSets().get(1).getPositionIndex());
  }
}
