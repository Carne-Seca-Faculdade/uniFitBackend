package com.nicolas.app_academy.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {
  private Long id;
  private String name;
  private String email;
  private String role;
  private Integer age;
  private Float height;
  private List<Long> trainingPlansIds;
  private String userIdentifier;

  public UserDTO(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.role = user.getRole();
    this.age = user.getAge();
    this.userIdentifier = user.getUserIdentifier();

    this.height = user.getHeight();
    this.trainingPlansIds = user.getTrainingPlans() != null
        ? user.getTrainingPlans().stream().map(TrainingPlans::getId).collect(Collectors.toList())
        : null;
  }
}
