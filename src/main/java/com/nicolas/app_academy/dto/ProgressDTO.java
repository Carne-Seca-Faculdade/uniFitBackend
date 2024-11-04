package com.nicolas.app_academy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.Progress;

@Data
@NoArgsConstructor
public class ProgressDTO implements Serializable {
  private Long id;
  private LocalDateTime monitoringStartedAt;
  private Float bodyWeight;
  private List<Long> exercisesPerformedIds;
  private Long userId;

  public ProgressDTO(Progress progress) {
    this.id = progress.getId();
    this.monitoringStartedAt = progress.getMonitoringStartedAt();
    this.bodyWeight = progress.getBodyWeight();
    this.exercisesPerformedIds = progress.getExercisesPerfomed() != null
        ? progress.getExercisesPerfomed().stream().map(Exercise::getId).collect(Collectors.toList())
        : null;
    this.userId = progress.getUser() != null ? progress.getUser().getId() : null;

  }
}
