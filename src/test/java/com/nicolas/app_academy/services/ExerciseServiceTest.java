package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.ExerciseDTO;
import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.Progress;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.repositories.ExerciseRepository;
import com.nicolas.app_academy.repositories.ProgressRepository;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExerciseServiceTest {

    @Autowired
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseRepository exerciseRepository;

    @MockBean
    private TrainingPlansRepository trainingPlansRepository;

    @MockBean
    private ProgressRepository progressRepository;

    private Exercise exercise;
    private ExerciseDTO exerciseDTO;
    private TrainingPlans trainingPlan;
    private Progress progress;

    @BeforeEach
    public void setUp() {
        exercise = new Exercise();
        exercise.setExerciseName("Agachamento");
        exercise.setExerciseDescription("Exercício para pernas");
        exercise.setExerciseWasPerformedAt(LocalDateTime.now());
        exercise.setSeriesQuantity(3);
        exercise.setRepetitionsQuantity(10);
        exercise.setWeightUsed(50);
        exercise.setRestTime(60);

        trainingPlan = new TrainingPlans();
        trainingPlan.setId(1L);

        progress = new Progress();
        progress.setId(1L);

        exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseName("Agachamento");
        exerciseDTO.setExerciseDescription("Exercício para pernas");
        exerciseDTO.setSeriesQuantity(3);
        exerciseDTO.setRepetitionsQuantity(10);
        exerciseDTO.setWeightUsed(50);
        exerciseDTO.setRestTime(60);
        exerciseDTO.setTrainingPlanId(1L);
        exerciseDTO.setProgressId(1L);
    }

    @Test
    public void shouldCriarExercicio() {
        when(trainingPlansRepository.findById(1L)).thenReturn(Optional.of(trainingPlan));
        when(progressRepository.findById(1L)).thenReturn(Optional.of(progress));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        ExerciseDTO result = exerciseService.criarExercicio(exerciseDTO);

        assertNotNull(result);
        assertEquals(exerciseDTO.getExerciseName(), result.getExerciseName());
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    public void shouldListarExercicios() {
        when(exerciseRepository.findAll()).thenReturn(Collections.singletonList(exercise));

        List<ExerciseDTO> result = exerciseService.listarExercicios();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(exercise.getExerciseName(), result.get(0).getExerciseName());
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    public void shouldAtualizarExercicio() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        ExerciseDTO result = exerciseService.atualizarExercicio(1L, exerciseDTO);

        assertNotNull(result);
        assertEquals(exerciseDTO.getExerciseName(), result.getExerciseName());
        verify(exerciseRepository, times(1)).findById(1L);
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    public void shouldDeletarExercicio() {
        when(exerciseRepository.existsById(1L)).thenReturn(true);

        exerciseService.deletarExercicio(1L);

        verify(exerciseRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoExercicioNaoEncontradoParaCriar() {
        when(trainingPlansRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                exerciseService.criarExercicio(exerciseDTO));

        assertEquals("Plano de treino nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoExercicioNaoEncontradoParaAtualizar() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                exerciseService.atualizarExercicio(1L, exerciseDTO));

        assertEquals("Exercicio nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoExercicioNaoEncontradoParaDeletar() {
        when(exerciseRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                exerciseService.deletarExercicio(1L));

        assertEquals("Exercicio nao encontrado", exception.getMessage());
    }
}
