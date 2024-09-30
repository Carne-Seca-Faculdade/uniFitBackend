package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.TrainingPlansDTO;
import com.nicolas.app_academy.services.TrainingPlansService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class TrainingPlansControllerTest {

  @Autowired
  private TrainingPlansController trainingPlansController;

  @MockBean
  private TrainingPlansService trainingPlansService;

  private TrainingPlansDTO trainingPlansDTO;

  @BeforeEach
  void setUp() {
    trainingPlansDTO = new TrainingPlansDTO();
    trainingPlansDTO.setUserIds(List.of(1L, 2L));
  }

  @Test
  void criarPlano_Success() {
    when(trainingPlansService.criarPlano(trainingPlansDTO, trainingPlansDTO.getUserIds())).thenReturn(trainingPlansDTO);

    ResponseEntity<TrainingPlansDTO> response = trainingPlansController.criarPlano(trainingPlansDTO);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(trainingPlansDTO, response.getBody());
    verify(trainingPlansService, times(1)).criarPlano(trainingPlansDTO, trainingPlansDTO.getUserIds());
  }

  @Test
  void listarPlanos_Success() {
    List<TrainingPlansDTO> planos = new ArrayList<>();
    planos.add(trainingPlansDTO);
    when(trainingPlansService.listarPlanos()).thenReturn(planos);

    ResponseEntity<List<TrainingPlansDTO>> response = trainingPlansController.listarPlanos();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(planos, response.getBody());
    verify(trainingPlansService, times(1)).listarPlanos();
  }

  @Test
  void atualizarPlano_Success() {
    Long trainingPlanId = 1L;
    when(trainingPlansService.atualizarPlano(trainingPlanId, trainingPlansDTO)).thenReturn(trainingPlansDTO);

    ResponseEntity<TrainingPlansDTO> response = trainingPlansController.atualizarPlano(trainingPlanId,
        trainingPlansDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(trainingPlansDTO, response.getBody());
    verify(trainingPlansService, times(1)).atualizarPlano(trainingPlanId, trainingPlansDTO);
  }

  @Test
  void deletarPlano_Success() {
    Long trainingPlanId = 1L;
    doNothing().when(trainingPlansService).deletarPlano(trainingPlanId);

    ResponseEntity<Void> response = trainingPlansController.deletarPlano(trainingPlanId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(trainingPlansService, times(1)).deletarPlano(trainingPlanId);
  }

  @Test
  void criarPlano_NotFound() {
    when(trainingPlansService.criarPlano(trainingPlansDTO, trainingPlansDTO.getUserIds()))
        .thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<TrainingPlansDTO> response = trainingPlansController.criarPlano(trainingPlansDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(trainingPlansService, times(1)).criarPlano(trainingPlansDTO, trainingPlansDTO.getUserIds());
  }

  @Test
  void atualizarPlano_NotFound() {
    Long trainingPlanId = 1L;
    when(trainingPlansService.atualizarPlano(trainingPlanId, trainingPlansDTO))
        .thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<TrainingPlansDTO> response = trainingPlansController.atualizarPlano(trainingPlanId,
        trainingPlansDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(trainingPlansService, times(1)).atualizarPlano(trainingPlanId, trainingPlansDTO);
  }

  @Test
  void deletarPlano_NotFound() {
    Long trainingPlanId = 1L;
    doThrow(new ResourceNotFoundException(null)).when(trainingPlansService).deletarPlano(trainingPlanId);

    ResponseEntity<Void> response = trainingPlansController.deletarPlano(trainingPlanId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(trainingPlansService, times(1)).deletarPlano(trainingPlanId);
  }
}
