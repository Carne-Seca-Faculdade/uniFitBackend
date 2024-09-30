package com.nicolas.app_academy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.Progress;
import com.nicolas.app_academy.entities.User;

@Data
@NoArgsConstructor
public class ProgressDTO implements Serializable {
  private Long id;
  private LocalDateTime monitoringStartedAt;
  private Float bodyWeight;
  private BodyMeasurementsDTO bodyMeasurements;
  private List<Long> exercisesPerformedIds;
  private List<Long> userIds;

  public ProgressDTO(Progress progress) {
    this.id = progress.getId();
    this.monitoringStartedAt = progress.getMonitoringStartedAt();
    this.bodyWeight = progress.getBodyWeight();
    this.bodyMeasurements = progress.getBodyMeasurements() != null
        ? new BodyMeasurementsDTO(progress.getBodyMeasurements())
        : null;
    this.exercisesPerformedIds = progress.getExercisesPerfomed() != null
        ? progress.getExercisesPerfomed().stream().map(Exercise::getId).collect(Collectors.toList())
        : null;
    this.userIds = progress.getUsers() != null
        ? progress.getUsers().stream().map(User::getId).collect(Collectors.toList())
        : null;
  }
}
