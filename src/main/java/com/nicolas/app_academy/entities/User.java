package com.nicolas.app_academy.entities;

import com.nicolas.app_academy.entities.enums.ObjectiveStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_user")
public class User {
  private Long id;
  private String name;
  private String email;
  private Integer age;
  private Float weight;
  private Float height;
  private ObjectiveStatus objective;
}
