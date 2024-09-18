package com.nicolas.app_academy.entities;

import com.nicolas.app_academy.entities.enums.ObjectiveStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
