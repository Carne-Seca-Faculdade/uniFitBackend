package com.nicolas.app_academy.entities;

import java.util.List;

import com.nicolas.app_academy.entities.enums.planDifficultyStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_trainingPlans")
public class TrainingPlans {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String planName;
  private String planDescription;

  @Enumerated
  private planDifficultyStatus difficultyStatus;

  @OneToMany(mappedBy = "trainingPlans", cascade = CascadeType.ALL)
  private List<Exercise> exerciseList;

  @ManyToMany(mappedBy = "trainingPlans")
  private List<User> users;
}
