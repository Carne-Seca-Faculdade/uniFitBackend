package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.ExerciseLogDTO;
import com.nicolas.app_academy.services.ExerciseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exercicioLog")
public class ExerciseLogController {

  @Autowired
  private ExerciseLogService exerciseLogService;

  @PostMapping("/start/{userId}/{workoutId}")
  public ResponseEntity<ExerciseLogDTO> iniciarTreino(
      @PathVariable Long userId,
      @PathVariable Long workoutId,
      @RequestBody ExerciseLogDTO exerciseLogDTO) {
    ExerciseLogDTO savedLog = exerciseLogService.iniciarTreino(userId, workoutId, exerciseLogDTO);
    return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
  }

  @PutMapping("/{exerciseLogId}/status")
  public ResponseEntity<ExerciseLogDTO> atualizarStatusTreino(
      @PathVariable Long exerciseLogId,
      @RequestParam String status) {
    ExerciseLogDTO updatedLog = exerciseLogService.atualizarStatusTreino(exerciseLogId, status);
    return ResponseEntity.ok(updatedLog);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ExerciseLogDTO>> listarExerciciosPorUsuario(@PathVariable Long userId) {
    List<ExerciseLogDTO> logs = exerciseLogService.listarExerciciosPorUsuario(userId);
    return ResponseEntity.ok(logs);
  }

  @GetMapping("/exercise/{exerciseId}")
  public ResponseEntity<List<ExerciseLogDTO>> listarExerciciosPorTreino(@PathVariable Long exerciseId) {
    List<ExerciseLogDTO> logs = exerciseLogService.listarExerciciosPorTreino(exerciseId);
    return ResponseEntity.ok(logs);
  }

  @GetMapping("/users/{userId}/weekly")
  public ResponseEntity<Map<String, Long>> exercisesCompletadosPorWeekly(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "4") int numberOfWeeks) {
    Map<String, Long> weeklyCounts = exerciseLogService.exercisesCompletadosPorWeekly(userId, numberOfWeeks);
    return ResponseEntity.ok(weeklyCounts);
  }
}
