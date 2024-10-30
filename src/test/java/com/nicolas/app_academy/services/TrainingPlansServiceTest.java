package com.nicolas.app_academy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.nicolas.app_academy.dto.ExerciseDTO;
import com.nicolas.app_academy.dto.TrainingPlansDTO;
import com.nicolas.app_academy.entities.Exercise;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.entities.enums.planDifficultyStatus;
import com.nicolas.app_academy.repositories.ExerciseRepository;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class TrainingPlansServiceTest {

    @InjectMocks
    private TrainingPlansService trainingPlansService;

    @Mock
    private TrainingPlansRepository trainingPlansRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private TrainingPlans trainingPlans;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("John Doe");
        user.setTrainingPlans(new ArrayList<>());

        trainingPlans = new TrainingPlans();
        trainingPlans.setPlanName("Full Body Workout");
        trainingPlans.setPlanDescription("A comprehensive workout plan.");
        trainingPlans.setDifficultyStatus(planDifficultyStatus.INTERMEDIATE);
    }

    @Test
    void testCriarPlano_Success() {
        // Prepare data
        List<Long> userIds = List.of(1L);
        ExerciseDTO exerciseDTO = new ExerciseDTO();
        exerciseDTO.setExerciseName("Squat");
        exerciseDTO.setExerciseDescription("Squat exercise");
        exerciseDTO.setSeriesQuantity(3);
        exerciseDTO.setRepetitionsQuantity(10);
        exerciseDTO.setWeightUsed(70);
        exerciseDTO.setRestTime(60);

        TrainingPlansDTO trainingPlanDTO = new TrainingPlansDTO();
        trainingPlanDTO.setPlanName("Plan 1");
        trainingPlanDTO.setPlanDescription("Description 1");
        trainingPlanDTO.setDifficultyStatus(planDifficultyStatus.INTERMEDIATE);
        trainingPlanDTO.setNewExercises(List.of(exerciseDTO));
        trainingPlanDTO.setExerciseIds(List.of(1L));

        when(userRepository.findAllById(anyList())).thenReturn(List.of(user));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(new Exercise()));
        when(trainingPlansRepository.save(any())).thenReturn(trainingPlans);

        TrainingPlansDTO result = trainingPlansService.criarPlano(trainingPlanDTO, userIds);

        assertNotNull(result);
        assertEquals("Full Body Workout", result.getPlanName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(exerciseRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testCriarPlano_UserNotFound() {
        List<Long> userIds = List.of(1L);
        TrainingPlansDTO trainingPlanDTO = new TrainingPlansDTO();

        when(userRepository.findAllById(anyList())).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> trainingPlansService.criarPlano(trainingPlanDTO, userIds));
    }

    @Test
    void testAtualizarPlano_Success() {
        Long trainingPlanId = 1L;
        TrainingPlansDTO trainingPlanDTO = new TrainingPlansDTO();
        trainingPlanDTO.setPlanName("Updated Plan");
        trainingPlanDTO.setPlanDescription("Updated Description");
        trainingPlanDTO.setDifficultyStatus(planDifficultyStatus.BEGINNER);

        when(trainingPlansRepository.findById(trainingPlanId)).thenReturn(Optional.of(trainingPlans));
        when(trainingPlansRepository.save(any())).thenReturn(trainingPlans);

        TrainingPlansDTO result = trainingPlansService.atualizarPlano(trainingPlanId, trainingPlanDTO);

        assertNotNull(result);
        assertEquals("Updated Plan", result.getPlanName());
        verify(trainingPlansRepository, times(1)).save(any(TrainingPlans.class));
    }

    @Test
    void testAtualizarPlano_NotFound() {
        Long trainingPlanId = 1L;
        TrainingPlansDTO trainingPlanDTO = new TrainingPlansDTO();

        when(trainingPlansRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> trainingPlansService.atualizarPlano(trainingPlanId, trainingPlanDTO));
    }

    @Test
    void testDeletarPlano_Success() {
        Long trainingPlanId = 1L;

        when(trainingPlansRepository.existsById(trainingPlanId)).thenReturn(true);

        trainingPlansService.deletarPlano(trainingPlanId);

        verify(trainingPlansRepository, times(1)).deleteById(trainingPlanId);
    }

    @Test
    void testDeletarPlano_NotFound() {
        Long trainingPlanId = 1L;

        when(trainingPlansRepository.existsById(trainingPlanId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> trainingPlansService.deletarPlano(trainingPlanId));
    }
}
