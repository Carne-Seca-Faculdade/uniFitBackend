package com.nicolas.app_academy.entities;

import java.util.List;

import com.nicolas.app_academy.entities.enums.ObjectiveStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;
  private Integer age;
  private Float weight;
  private Float height;
  private ObjectiveStatus objective;

  @ManyToOne
  @JoinColumn(name = "progress_id")
  private Progress progress;

  @ManyToMany
  @JoinTable(name = "tb_user_trainingPlans", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "trainingPlans_id"))
  private List<TrainingPlans> trainingPlans;

  @OneToOne
  @JoinColumn(name = "bodyMeasurements_id")
  private BodyMeasurements bodyMeasurements;
}
