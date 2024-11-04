package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.ExerciseDTO;
import com.nicolas.app_academy.dto.ExerciseLogDTO;
import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.ExerciseLog;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.ExerciseLogRepository;
import com.nicolas.app_academy.repositories.ExerciseRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExerciseLogService {

  @Autowired
  private ExerciseLogRepository exerciseLogRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  public ExerciseLogDTO iniciarTreino(Long userId, Long exerciseId, ExerciseLogDTO exerciseLogDTO) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));

    Exercise exercise = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new IllegalArgumentException("Treino nao encontrado"));

    ExerciseLog exerciseLog = new ExerciseLog();
    exerciseLog.setUser(user);
    exerciseLog.setExercise(exercise);
    exerciseLog.setExercise(exerciseLogDTO.getExercise());
    exerciseLog.setPerformedAt(LocalDateTime.now());
    exerciseLog.setSeries(exerciseLogDTO.getSeries());
    exerciseLog.setRepetitions(exerciseLogDTO.getRepetitions());
    exerciseLog.setWeight(exerciseLogDTO.getWeight());
    exerciseLog.setDuration(exerciseLogDTO.getDuration());
    exerciseLog.setStatus("Iniciado");

    ExerciseLog savedExerciseLog = exerciseLogRepository.save(exerciseLog);
    return new ExerciseLogDTO(savedExerciseLog);
  }

  public ExerciseLogDTO atualizarStatusTreino(Long exerciseLogId, String status) {
    ExerciseLog exerciseLog = exerciseLogRepository.findById(exerciseLogId)
        .orElseThrow(() -> new IllegalArgumentException("Log de exercicio nao encontrado"));

    exerciseLog.setStatus(status);
    if ("Concluido".equals(status)) {
      exerciseLog.setPerformedAt(LocalDateTime.now());
    }

    ExerciseLog updatedExerciseLog = exerciseLogRepository.save(exerciseLog);
    return new ExerciseLogDTO(updatedExerciseLog);
  }

  public List<ExerciseLogDTO> listarExerciciosPorUsuario(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
    List<ExerciseLog> logs = exerciseLogRepository.findByUser(user);
    return logs.stream().map(ExerciseLogDTO::new).collect(Collectors.toList());
  }

  public List<ExerciseLogDTO> listarExerciciosPorTreino(Long exerciseId) {
    Exercise exercise = exerciseRepository.findById(exerciseId)
        .orElseThrow(() -> new IllegalArgumentException("Treino nao encontrado"));
    List<ExerciseLog> logs = exerciseLogRepository.findByExercise(exercise);
    return logs.stream().map(ExerciseLogDTO::new).collect(Collectors.toList());
  }

  public ExerciseLogDTO completeExercise(Long logId, Integer duration, ExerciseDTO exerciseDTO) {
    ExerciseLog exerciseLog = exerciseLogRepository.findById(logId)
        .orElseThrow(() -> new ResourceNotFoundException("Exercise log nao encontrado"));

    Exercise exercise = exerciseRepository.findById(exerciseDTO.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Exercise nao encontrado"));

    exerciseLog.setStatus("Concluido");
    exerciseLog.setPerformedAt(LocalDateTime.now());
    exerciseLog.setDuration(duration);
    exerciseLog.setExercise(exercise);

    exerciseLogRepository.save(exerciseLog);

    return new ExerciseLogDTO(exerciseLog);
  }

  public Long countUserCompletedWorkouts(Long userId) {
    return exerciseLogRepository.countByUserIdAndStatus(userId, "Concluido");
  }

  public Map<String, Long> exercisesCompletadosPorWeekly(Long userId, int numWeeks) {
    Map<String, Long> countsSemanas = new LinkedHashMap<>();
    LocalDateTime endDate = LocalDateTime.now();

    for (int i = 0; i < numWeeks; i++) {
      LocalDateTime startDate = endDate.minusWeeks(1);
      Long count = exerciseLogRepository.countConcluidoExercisesByUserAndDateRange(userId, startDate, endDate);

      String weekParse = startDate.toLocalDate() + " to " + endDate.toLocalDate();
      countsSemanas.put(weekParse, count);

      endDate = startDate;
    }

    return countsSemanas;
  }
}
