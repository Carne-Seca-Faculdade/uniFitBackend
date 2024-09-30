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
  private Float weight;
  private Float height;
  private ObjectiveStatus objective;
  private ProgressDTO progress;
  private List<Long> trainingPlansIds;
  private BodyMeasurementsDTO bodyMeasurements;

  public UserDTO(Long id, String name, String email, Integer age, Float weight, Float height, ObjectiveStatus objective,
      ProgressDTO progress, List<Long> trainingPlansIds, BodyMeasurementsDTO bodyMeasurements) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.age = age;
    this.weight = weight;
    this.height = height;
    this.objective = objective;
    this.progress = progress;
    this.trainingPlansIds = trainingPlansIds;
    this.bodyMeasurements = bodyMeasurements;
  }

  public UserDTO(User user) {
    this(user.getId(), user.getName(), user.getEmail(), user.getAge(), user.getWeight(), user.getHeight(),
        user.getObjective() != null ? user.getObjective() : null,
        user.getProgress() != null ? new ProgressDTO(user.getProgress()) : null,
        user.getTrainingPlans() != null
            ? user.getTrainingPlans().stream().map(TrainingPlans::getId).collect(Collectors.toList())
            : null,
        user.getBodyMeasurements() != null ? new BodyMeasurementsDTO(user.getBodyMeasurements()) : null);
  }
}
