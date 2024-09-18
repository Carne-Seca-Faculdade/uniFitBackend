package com.nicolas.app_academy.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_progress")
public class Progress {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private User user;
  private LocalDateTime monitoringStartedAt;
  private Float bodyWeight;
  private BodyMeasurements bodyMeasurements;
  private Exercise exercisesPerfomed;

}
