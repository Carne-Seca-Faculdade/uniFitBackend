package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.dto.WeightDTO;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.entities.Weight;
import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Usuário Teste");
        userDTO.setEmail("usuario@test.com");
        userDTO.setAge(25);
        userDTO.setHeight(175.0f);

        WeightDTO weightDTO = new WeightDTO();
        weightDTO.setValue(70.0f);
        userDTO.setWeight(weightDTO);

        user = new User();
        user.setId(1L);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setHeight(userDTO.getHeight());

        Weight weight = new Weight();
        weight.setPeso(70.0f);
        user.setWeight(weight);
    }

    @Test
    public void shouldThrowExceptionWhenCreatingUserFails() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Erro ao criar usuário"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.criarUser(userDTO));

        assertEquals("Erro ao criar usuário", exception.getMessage());
    }

    @Test
    public void shouldListUsersSuccessfully() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDTO> result = userService.listarUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getName(), result.get(0).getName());
        assertEquals(userDTO.getWeight().getValue(), result.get(0).getWeight().getValue());
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
    public void shouldUpdateUserSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.atualizarUser(1L, userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getName(), result.getName());
        assertEquals(userDTO.getWeight().getValue(), result.getWeight().getValue());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenUserNotFoundForUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.atualizarUser(1L, userDTO));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
        when(userRepository.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.deletarUser(1L));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldCalculateIMCSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.calcularIMC(1L);

        assertEquals("22,86", result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWhenUserNotFoundForIMC() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.calcularIMC(1L));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenHeightIsZeroOrLess() {
        user.setHeight(0.0f);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.calcularIMC(1L));

        assertEquals("Altura precisa ser maior que zero", exception.getMessage());
    }

    @Test
    public void shouldAssignTrainingPlansToUserSuccessfully() {
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
    public void shouldThrowResourceNotFoundExceptionWhenNoTrainingPlansFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(trainingPlansRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.setTrainingPlansForUser(1L, Collections.singletonList(1L)));

        assertEquals("Nenhum plano de treino encontrado para os IDs fornecidos", exception.getMessage());
    }

    @Test
    public void shouldDetermineHealthStatusSuccessfully() {
        String imcValue = "22.86";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Você está com peso normal.", result);
    }

    @Test
    public void shouldReturnInvalidValueMessageWhenIMCIsInvalid() {
        String imcValue = "abc";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Valor de IMC inválido.", result);
    }

    @Test
    public void shouldReturnUnderweightMessageWhenIMCLessThan18_5() {
        String imcValue = "18.0";
        String result = userService.determinarEstadoDeSaude(imcValue);

        assertEquals("Você está abaixo do peso.", result);
    }

    @Test
    public void shouldReturnOverweightMessageWhenIMCIsBetween25And29_9() {
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
