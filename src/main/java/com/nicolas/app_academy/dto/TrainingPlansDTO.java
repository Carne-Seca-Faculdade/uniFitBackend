package com.nicolas.app_academy.dto;

import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TrainingPlansDTO implements Serializable {
  private Long id;
  private String planName;
  private String planDescription;
  private Integer duration;
  private List<Long> exerciseIds;
  private List<Long> userIds;
  private List<ExerciseDTO> newExercises;

  public TrainingPlansDTO(TrainingPlans trainingPlans, boolean includeNewExercises) {
    this.id = trainingPlans.getId();
    this.planName = trainingPlans.getPlanName();
    this.planDescription = trainingPlans.getPlanDescription();
    this.duration = trainingPlans.getDuration();
    this.exerciseIds = trainingPlans.getExerciseList() != null
        ? trainingPlans.getExerciseList().stream().map(Exercise::getId).collect(Collectors.toList())
        : null;
    this.userIds = trainingPlans.getUsers() != null
        ? trainingPlans.getUsers().stream().map(User::getId).collect(Collectors.toList())
        : null;

    if (includeNewExercises && trainingPlans.getExerciseList() != null) {
      this.newExercises = trainingPlans.getExerciseList().stream()
          .map(ExerciseDTO::new)
          .collect(Collectors.toList());
    }
  }

  public TrainingPlansDTO(TrainingPlans trainingPlans) {
    this.id = trainingPlans.getId();
    this.planName = trainingPlans.getPlanName();
    this.planDescription = trainingPlans.getPlanDescription();
    this.duration = trainingPlans.getDuration();
    this.exerciseIds = trainingPlans.getExerciseList() != null
        ? trainingPlans.getExerciseList().stream().map(Exercise::getId).collect(Collectors.toList())
        : null;
    this.userIds = trainingPlans.getUsers() != null
        ? trainingPlans.getUsers().stream().map(User::getId).collect(Collectors.toList())
        : null;
  }
}
