package com.nicolas.app_academy.dto;

import java.time.LocalDateTime;
import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.ExerciseLog;
import com.nicolas.app_academy.entities.User;

public class ExerciseLogDTO {

  private Long id;
  private User user;
  private Exercise exercise;
  private LocalDateTime performedAt;
  private Integer series;
  private Integer repetitions;
  private Integer weight;
  private Integer duration;
  private String status;

  public ExerciseLogDTO() {
  }

  public ExerciseLogDTO(ExerciseLog exerciseLog) {
    this.id = exerciseLog.getId();
    this.user = exerciseLog.getUser();
    this.exercise = exerciseLog.getExercise();
    this.performedAt = exerciseLog.getPerformedAt();
    this.series = exerciseLog.getSeries();
    this.repetitions = exerciseLog.getRepetitions();
    this.weight = exerciseLog.getWeight();
    this.duration = exerciseLog.getDuration();
    this.status = exerciseLog.getStatus();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Exercise getExercise() {
    return exercise;
  }

  public void setExercise(Exercise exercise) {
    this.exercise = exercise;
  }

  public LocalDateTime getPerformedAt() {
    return performedAt;
  }

  public void setPerformedAt(LocalDateTime performedAt) {
    this.performedAt = performedAt;
  }

  public Integer getSeries() {
    return series;
  }

  public void setSeries(Integer series) {
    this.series = series;
  }

  public Integer getRepetitions() {
    return repetitions;
  }

  public void setRepetitions(Integer repetitions) {
    this.repetitions = repetitions;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
