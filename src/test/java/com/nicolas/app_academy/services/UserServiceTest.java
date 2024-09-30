package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.entities.enums.ObjectiveStatus;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TrainingPlansRepository trainingPlansRepository;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Usuário Teste");
        user.setEmail("usuario@test.com");
        user.setAge(25);
        user.setWeight(70.0f);
        user.setHeight(175.0f);
        user.setObjective(ObjectiveStatus.LOSS_WEIGHT);

        userDTO = new UserDTO();
        userDTO.setName("Usuário Teste");
        userDTO.setEmail("usuario@test.com");
        userDTO.setAge(25);
        userDTO.setWeight(70.0f);
        userDTO.setHeight(175.0f);
        userDTO.setObjective(ObjectiveStatus.LOSS_WEIGHT);
    }

    @Test
    public void shouldCriarUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.criarUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenCriarUserFails() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Erro ao criar usuário"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.criarUser(userDTO));

        assertEquals("Erro ao criar usuário", exception.getMessage());
    }

    @Test
    public void shouldListarUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDTO> result = userService.listarUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getName(), result.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.listarUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void shouldAtualizarUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.atualizarUser(1L, userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoAtualizarUserNaoEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.atualizarUser(1L, userDTO));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldDeletarUserSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deletarUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoDeletarUserNaoEncontrado() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.deletarUser(1L));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldCalcularIMCSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.calcularIMC(1L);

        assertEquals("22.86", result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoCalcularIMCUsuarioNaoEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.calcularIMC(1L));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionQuandoAlturaMenorOuIgualAZero() {
        user.setHeight(0.0f);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.calcularIMC(1L));

        assertEquals("Altura precisa ser maior que zero", exception.getMessage());
    }

    @Test
    public void shouldSetTrainingPlansForUserSuccessfully() {
        List<Long> trainingPlanIds = Collections.singletonList(1L);
        TrainingPlans trainingPlan = new TrainingPlans();
        trainingPlan.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(trainingPlansRepository.findAllById(trainingPlanIds)).thenReturn(Collections.singletonList(trainingPlan));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.setTrainingPlansForUser(1L, trainingPlanIds);

        verify(userRepository, times(1)).findById(1L);
        verify(trainingPlansRepository, times(1)).findAllById(trainingPlanIds);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoNenhumPlanoDeTreinoEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(trainingPlansRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.setTrainingPlansForUser(1L, Collections.singletonList(1L)));

        assertEquals("Nenhum plano de treino encontrado para os IDs fornecidos", exception.getMessage());
    }

    @Test
    public void shouldDeterminarEstadoDeSaudeSuccessfully() {
        String imcValue = "22.86";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Você está com peso normal.", result);
    }

    @Test
    public void shouldReturnInvalidValueMessageWhenDeterminarEstadoDeSaudeWithInvalidIMC() {
        String imcValue = "abc";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Valor de IMC inválido.", result);
    }

    @Test
    public void shouldReturnBelowWeightMessageWhenIMCLessThan18_5() {
        String imcValue = "18.0";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Você está abaixo do peso.", result);
    }

    @Test
    public void shouldReturnOverweightMessageWhenIMCBetween25And29_9() {
        String imcValue = "27.0";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Você está com sobrepeso.", result);
    }

    @Test
    public void shouldReturnObesityMessageWhenIMCIsGreaterThan29_9() {
        String imcValue = "30.0";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Você está com obesidade.", result);
    }
}
