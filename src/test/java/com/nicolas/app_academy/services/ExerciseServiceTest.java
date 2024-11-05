package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.ExerciseDTO;
import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.repositories.ExerciseRepository;
import com.nicolas.app_academy.repositories.ProgressRepository;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class ExerciseServiceTest {

    @InjectMocks
    private ExerciseService exerciseService;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private TrainingPlansRepository trainingPlansRepository;

    @Mock
    private ProgressRepository progressRepository;

    private ExerciseDTO exerciseDTO;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseName("Push Up");
        exerciseDTO.setExerciseDescription("An upper body exercise");
        exerciseDTO.setSeriesQuantity(3);
        exerciseDTO.setRepetitionsQuantity(10);
        exerciseDTO.setWeightUsed(10);
        exerciseDTO.setRestTime(60);

        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setExerciseName("Push Up");
        exercise.setExerciseDescription("An upper body exercise");
        exercise.setSeriesQuantity(3);
        exercise.setRepetitionsQuantity(10);
        exercise.setWeightUsed(10);
        exercise.setRestTime(60);
    }

    @Test
    void criarExercicio_Success() {
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);
        when(trainingPlansRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(progressRepository.findById(anyLong())).thenReturn(Optional.empty());

        ExerciseDTO createdExercise = exerciseService.criarExercicio(exerciseDTO);

        assertNotNull(createdExercise);
        assertEquals(exerciseDTO.getExerciseName(), createdExercise.getExerciseName());
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    void criarExercicio_WithTrainingPlan_Success() {
        TrainingPlans trainingPlan = new TrainingPlans();
        trainingPlan.setId(1L);
        exerciseDTO.setTrainingPlanId(1L);

        when(trainingPlansRepository.findById(exerciseDTO.getTrainingPlanId())).thenReturn(Optional.of(trainingPlan));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        ExerciseDTO createdExercise = exerciseService.criarExercicio(exerciseDTO);

        assertNotNull(createdExercise);
        verify(trainingPlansRepository, times(1)).findById(exerciseDTO.getTrainingPlanId());
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    void listarExercicios_Success() {
        when(exerciseRepository.findAll()).thenReturn(List.of(exercise));

        List<ExerciseDTO> exercises = exerciseService.listarExercicios();

        assertNotNull(exercises);
        assertEquals(1, exercises.size());
        assertEquals(exercise.getExerciseName(), exercises.get(0).getExerciseName());
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void atualizarExercicio_Success() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        ExerciseDTO updatedExercise = exerciseService.atualizarExercicio(1L, exerciseDTO);

        assertNotNull(updatedExercise);
        assertEquals(exerciseDTO.getExerciseName(), updatedExercise.getExerciseName());
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    void atualizarExercicio_NotFound() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            exerciseService.atualizarExercicio(1L, exerciseDTO);
        });
    }

    @Test
    void deletarExercicio_Success() {
        when(exerciseRepository.existsById(1L)).thenReturn(true);

        exerciseService.deletarExercicio(1L);

        verify(exerciseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarExercicio_NotFound() {
        when(exerciseRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            exerciseService.deletarExercicio(1L);
        });
    }
}
