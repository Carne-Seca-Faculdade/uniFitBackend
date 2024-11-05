package com.nicolas.app_academy.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.entities.enums.ObjectiveStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {
  private Long id;
  private String name;
  private String email;
  private Integer age;
  private WeightDTO weight;
  private Float height;
  private ObjectiveStatus objective;
  private ProgressDTO progress;
  private List<Long> trainingPlansIds;

  public UserDTO(Long id, String name, String email, Integer age, WeightDTO weight, Float height,
      ObjectiveStatus objective,
      ProgressDTO progress, List<Long> trainingPlansIds) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.age = age;
    this.weight = weight;
    this.height = height;
    this.objective = objective;
    this.progress = progress;
    this.trainingPlansIds = trainingPlansIds;
  }

  public UserDTO(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.age = user.getAge();

    this.weight = user.getWeight() != null ? new WeightDTO(user.getWeight()) : null;

    this.height = user.getHeight();
    this.objective = user.getObjective();
    this.progress = user.getProgress() != null ? new ProgressDTO(user.getProgress()) : null;

    this.trainingPlansIds = user.getTrainingPlans() != null
        ? user.getTrainingPlans().stream().map(TrainingPlans::getId).collect(Collectors.toList())
        : null;
  }
}
