package com.nicolas.app_academy.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_progress")
public class Progress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime monitoringStartedAt;
  private Float bodyWeight;

  @OneToOne
  @JoinColumn(name = "bodyMeasurements_id")
  private BodyMeasurements bodyMeasurements;

  @OneToMany(mappedBy = "progress")
  private List<Exercise> exercisesPerfomed;

  @OneToMany(mappedBy = "progress")
  private List<User> users;

}
