package com.nicolas.app_academy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import com.nicolas.app_academy.dto.ExerciseDTO;
import com.nicolas.app_academy.dto.TrainingPlansDTO;
import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.ExerciseRepository;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;

@Service
public class TrainingPlansService {

  @Autowired
  private TrainingPlansRepository trainingPlansRepository;

  @Autowired
  private ExerciseRepository exerciseRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExerciseService exerciseService;

  public TrainingPlansDTO criarPlano(TrainingPlansDTO trainingPlanDTO, List<Long> userIds) {
    List<User> users = userRepository.findAllById(userIds);

    if (users.size() != userIds.size()) {
      throw new ResourceNotFoundException("Um ou mais usuarios nao foram encontrados.");
    }

    for (User user : users) {
      if (user.getTrainingPlans().size() >= 3) {
        throw new IllegalArgumentException("Usuario " + user.getName() + " ja possui 3 planos de treino ativos.");
      }
    }

    TrainingPlans trainingPlan = new TrainingPlans();
    trainingPlan.setPlanName(trainingPlanDTO.getPlanName());
    trainingPlan.setPlanDescription(trainingPlanDTO.getPlanDescription());
    trainingPlan.setDuration(trainingPlanDTO.getDuration());

    if (!users.isEmpty()) {
      trainingPlan.setUsers(users);
    } else {
      trainingPlan.setUsers(new ArrayList<>());
    }

    List<Exercise> exercises = new ArrayList<>();

    if (trainingPlanDTO.getExerciseIds() != null && !trainingPlanDTO.getExerciseIds().isEmpty()) {
      for (Long exerciseId : trainingPlanDTO.getExerciseIds()) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
            .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado"));

        existingExercise.setTrainingPlans(trainingPlan);
        exercises.add(existingExercise);
      }
    }

    TrainingPlans trainingPlanSaved = trainingPlansRepository.save(trainingPlan);

    for (User user : users) {
      user.getTrainingPlans().add(trainingPlanSaved);
      userRepository.save(user);
    }

    if (trainingPlanDTO.getNewExercises() != null && !trainingPlanDTO.getNewExercises().isEmpty()) {
      for (ExerciseDTO newExerciseDTO : trainingPlanDTO.getNewExercises()) {
        Exercise newExercise = new Exercise();
        newExercise.setExerciseName(newExerciseDTO.getExerciseName());
        newExercise.setExerciseDescription(newExerciseDTO.getExerciseDescription());
        newExercise.setSeriesQuantity(newExerciseDTO.getSeriesQuantity());
        newExercise.setRepetitionsQuantity(newExerciseDTO.getRepetitionsQuantity());
        newExercise.setWeightUsed(newExerciseDTO.getWeightUsed());
        newExercise.setRestTime(newExerciseDTO.getRestTime());
        newExercise.setTrainingPlans(trainingPlanSaved);

        exercises.add(newExercise);
      }
    }

    trainingPlanSaved.setExerciseList(exercises);
    exerciseRepository.saveAll(exercises);
    trainingPlansRepository.save(trainingPlanSaved);

    return new TrainingPlansDTO(trainingPlanSaved, true);
  }

  public List<TrainingPlansDTO> listarPlanos() {
    return trainingPlansRepository.findAll().stream().map(plan -> {
      return new TrainingPlansDTO(plan, false);
    }).collect(Collectors.toList());
  }

  public TrainingPlansDTO atualizarPlano(Long trainingPlanId, TrainingPlansDTO trainingPlanDTO) {
    TrainingPlans trainingPlan = trainingPlansRepository.findById(trainingPlanId)
        .orElseThrow(() -> new ResourceNotFoundException("Plano de treino nao encontrado"));

    trainingPlan.setPlanName(trainingPlanDTO.getPlanName());
    trainingPlan.setPlanDescription(trainingPlanDTO.getPlanDescription());
    trainingPlan.setDuration(trainingPlanDTO.getDuration());
    
    List<Exercise> updatedExercises = new ArrayList<>();  

    if (trainingPlanDTO.getExerciseIds() != null && !trainingPlanDTO.getExerciseIds().isEmpty()) {
      for (Long exerciseId : trainingPlanDTO.getExerciseIds()) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
            .orElseThrow(() -> new ResourceNotFoundException("Exercicio nao encontrado"));
        existingExercise.setTrainingPlans(trainingPlan);
        updatedExercises.add(existingExercise);
      }
    }

    if (trainingPlanDTO.getNewExercises() != null && !trainingPlanDTO.getNewExercises().isEmpty()) {
      for (ExerciseDTO newExerciseDTO : trainingPlanDTO.getNewExercises()) {
        Exercise newExercise = new Exercise();
        newExercise.setExerciseName(newExerciseDTO.getExerciseName());
        newExercise.setExerciseDescription(newExerciseDTO.getExerciseDescription());
        newExercise.setSeriesQuantity(newExerciseDTO.getSeriesQuantity());
        newExercise.setRepetitionsQuantity(newExerciseDTO.getRepetitionsQuantity());
        newExercise.setWeightUsed(newExerciseDTO.getWeightUsed());
        newExercise.setRestTime(newExerciseDTO.getRestTime());
        newExercise.setTrainingPlans(trainingPlan);

        updatedExercises.add(newExercise);
      }
    }

    trainingPlan.setExerciseList(updatedExercises);
    exerciseRepository.saveAll(updatedExercises);
    trainingPlansRepository.save(trainingPlan);

    return new TrainingPlansDTO(trainingPlan, true);
  }

  public void deletarPlano(Long trainingPlanId) {
    if (!trainingPlansRepository.existsById(trainingPlanId)) {
      throw new ResourceNotFoundException("Plano de treino nao encontrado");
    }
    trainingPlansRepository.deleteById(trainingPlanId);
  }

  public ExerciseDTO addExerciseToTrainingPlan(Long trainingPlanId, ExerciseDTO exerciseDTO) {
    TrainingPlans trainingPlan = trainingPlansRepository.findById(trainingPlanId)
        .orElseThrow(() -> new ResourceNotFoundException("Plano de treino nao encontrado"));

    Exercise newExercise = new Exercise();
    newExercise.setExerciseName(exerciseDTO.getExerciseName());
    newExercise.setExerciseDescription(exerciseDTO.getExerciseDescription());
    newExercise.setSeriesQuantity(exerciseDTO.getSeriesQuantity());
    newExercise.setRepetitionsQuantity(exerciseDTO.getRepetitionsQuantity());
    newExercise.setWeightUsed(exerciseDTO.getWeightUsed());
    newExercise.setRestTime(exerciseDTO.getRestTime());

    newExercise.setTrainingPlans(trainingPlan);

    exerciseRepository.save(newExercise);

    trainingPlan.getExerciseList().add(newExercise);
    trainingPlansRepository.save(trainingPlan);

    return new ExerciseDTO(newExercise); 
  }

  public TrainingPlansDTO getTrainingPlanById(Long id) {
    TrainingPlans trainingPlan = trainingPlansRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Plano de treino nao encontrado"));

    List<ExerciseDTO> exercises = exerciseService.getExercisesByIds(trainingPlan.getExerciseIds());

    TrainingPlansDTO trainingPlanDTO = new TrainingPlansDTO(trainingPlan);
    trainingPlanDTO.setNewExercises(exercises);
    return trainingPlanDTO;
  }

}
