package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.TrainingPlansDTO;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.entities.enums.planDifficultyStatus;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrainingPlansServiceTest {

    @Autowired
    private TrainingPlansService trainingPlansService;

    @MockBean
    private TrainingPlansRepository trainingPlansRepository;

    @MockBean
    private UserRepository userRepository;

    private TrainingPlans trainingPlan;
    private TrainingPlansDTO trainingPlanDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Usuário Teste");
        user.setTrainingPlans(new ArrayList<>());

        trainingPlan = new TrainingPlans();
        trainingPlan.setPlanName("Plano de Treino Teste");
        trainingPlan.setPlanDescription("Descrição do Plano de Treino Teste");
        trainingPlan.setDifficultyStatus(planDifficultyStatus.BEGINNER);

        trainingPlanDTO = new TrainingPlansDTO();
        trainingPlanDTO.setPlanName("Plano de Treino Teste");
        trainingPlanDTO.setPlanDescription("Descrição do Plano de Treino Teste");
        trainingPlanDTO.setDifficultyStatus(planDifficultyStatus.BEGINNER);
        trainingPlanDTO.setExerciseIds(Collections.emptyList());
        trainingPlanDTO.setNewExercises(Collections.emptyList());
    }

    @Test
    public void shouldCriarPlano() {
        when(userRepository.findAllById(Collections.singletonList(1L))).thenReturn(Collections.singletonList(user));
        when(trainingPlansRepository.save(any(TrainingPlans.class))).thenReturn(trainingPlan);

        TrainingPlansDTO result = trainingPlansService.criarPlano(trainingPlanDTO, Collections.singletonList(1L));

        assertNotNull(result);
        assertEquals(trainingPlanDTO.getPlanName(), result.getPlanName());
        verify(trainingPlansRepository, times(1)).save(any(TrainingPlans.class));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoUsuarioNaoEncontrado() {
        when(userRepository.findAllById(Collections.singletonList(1L))).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                trainingPlansService.criarPlano(trainingPlanDTO, Collections.singletonList(1L)));

        assertEquals("Um ou mais usuários não foram encontrados.", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionQuandoUsuarioTemMaisDeTresPlanosAtivos() {
        User userWithPlans = new User();
        userWithPlans.setId(2L);
        userWithPlans.setTrainingPlans(new ArrayList<>(Collections.nCopies(3, trainingPlan)));

        when(userRepository.findAllById(Collections.singletonList(2L))).thenReturn(Collections.singletonList(userWithPlans));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                trainingPlansService.criarPlano(trainingPlanDTO, Collections.singletonList(2L)));

        assertEquals("Usuario " + userWithPlans.getName() + " ja possui 3 planos de treino ativos.", exception.getMessage());
    }

    @Test
    public void shouldListarPlanos() {
        when(trainingPlansRepository.findAll()).thenReturn(Collections.singletonList(trainingPlan));

        List<TrainingPlansDTO> result = trainingPlansService.listarPlanos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainingPlanDTO.getPlanName(), result.get(0).getPlanName());
        verify(trainingPlansRepository, times(1)).findAll();
    }

    @Test
    public void shouldAtualizarPlano() {
        when(trainingPlansRepository.findById(1L)).thenReturn(Optional.of(trainingPlan));
        when(trainingPlansRepository.save(any(TrainingPlans.class))).thenReturn(trainingPlan);

        TrainingPlansDTO result = trainingPlansService.atualizarPlano(1L, trainingPlanDTO);

        assertNotNull(result);
        assertEquals(trainingPlanDTO.getPlanName(), result.getPlanName());
        verify(trainingPlansRepository, times(1)).findById(1L);
        verify(trainingPlansRepository, times(1)).save(any(TrainingPlans.class));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoPlanoNaoEncontradoParaAtualizar() {
        when(trainingPlansRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                trainingPlansService.atualizarPlano(1L, trainingPlanDTO));

        assertEquals("Plano de treino nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldDeletarPlano() {
        when(trainingPlansRepository.existsById(1L)).thenReturn(true);

        trainingPlansService.deletarPlano(1L);

        verify(trainingPlansRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoPlanoNaoEncontradoParaDeletar() {
        when(trainingPlansRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                trainingPlansService.deletarPlano(1L));

        assertEquals("Plano de treino nao encontrado", exception.getMessage());
    }
}
