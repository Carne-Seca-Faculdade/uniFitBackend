package com.nicolas.app_academy.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_exercise")
public class Exercise {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String exerciseName;
  private String exerciseDescription;
  private LocalDateTime exerciseWasPerformedAt;
  private Integer seriesQuantity;
  private Integer repetitionsQuantity;
  private Integer weightUsed;
  private Integer restTime;

  @ManyToOne
  private TrainingPlans trainingPlans;

  @ManyToOne
  private Progress progress;
}
