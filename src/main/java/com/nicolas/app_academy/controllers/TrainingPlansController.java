package com.nicolas.app_academy.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicolas.app_academy.dto.TrainingPlansDTO;
import com.nicolas.app_academy.services.TrainingPlansService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/planos-treino")
public class TrainingPlansController {
  @Autowired
  private TrainingPlansService trainingPlansService;

  @PostMapping("/save")
  public ResponseEntity<TrainingPlansDTO> criarPlano(@RequestBody TrainingPlansDTO trainingPlanDTO) {
    try {
      TrainingPlansDTO createdPlan = trainingPlansService.criarPlano(trainingPlanDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping
  public ResponseEntity<List<TrainingPlansDTO>> listarPlanos() {
    try {
      List<TrainingPlansDTO> planos = trainingPlansService.listarPlanos();
      return ResponseEntity.status(HttpStatus.OK).body(planos);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/{trainingPlanId}")
  public ResponseEntity<TrainingPlansDTO> atualizarPlano(@PathVariable Long trainingPlanId,
      @RequestBody TrainingPlansDTO trainingPlansDTO) {
    try {
      TrainingPlansDTO updatedPlan = trainingPlansService.atualizarPlano(trainingPlanId, trainingPlansDTO);
      return ResponseEntity.status(HttpStatus.OK).body(updatedPlan);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/{trainingPlanId}")
  public ResponseEntity<Void> deletarPlano(@PathVariable Long trainingPlanId) {
    try {
      trainingPlansService.deletarPlano(trainingPlanId);
      return ResponseEntity.noContent().build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
