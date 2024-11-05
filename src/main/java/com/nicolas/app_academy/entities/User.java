package com.nicolas.app_academy.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
  private Float height;

  @OneToOne(mappedBy = "user")
  private Progress progress;

  @OneToOne(mappedBy = "user")
  private Weight weight;

  @ManyToMany
  @JoinTable(name = "tb_user_trainingPlans", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "trainingPlans_id"))
  private List<TrainingPlans> trainingPlans;
}
