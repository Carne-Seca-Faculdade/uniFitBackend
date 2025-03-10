package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.ExerciseDTO;
import com.nicolas.app_academy.services.ExerciseService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExerciseControllerTest {

  @Autowired
  private ExerciseController exerciseController;

  @MockBean
  private ExerciseService exerciseService;

  private ExerciseDTO exerciseDTO;

  @BeforeEach
  void setUp() {
    exerciseDTO = new ExerciseDTO();
  }

  @Test
  void criarExercicio_Success() {
    when(exerciseService.criarExercicio(exerciseDTO)).thenReturn(exerciseDTO);

    ResponseEntity<ExerciseDTO> response = exerciseController.criarExercicio(exerciseDTO);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(exerciseDTO, response.getBody());
    verify(exerciseService, times(1)).criarExercicio(exerciseDTO);
  }

  @Test
  void listarExercicios_Success() {
    List<ExerciseDTO> exercises = new ArrayList<>();
    exercises.add(exerciseDTO);
    when(exerciseService.listarExercicios()).thenReturn(exercises);

    ResponseEntity<List<ExerciseDTO>> response = exerciseController.listarExercicios();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(exercises, response.getBody());
    verify(exerciseService, times(1)).listarExercicios();
  }

  @Test
  void atualizarExercicio_Success() {
    Long exerciseId = 1L;
    when(exerciseService.atualizarExercicio(exerciseId, exerciseDTO)).thenReturn(exerciseDTO);

    ResponseEntity<ExerciseDTO> response = exerciseController.atualizarExercicio(exerciseId, exerciseDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(exerciseDTO, response.getBody());
    verify(exerciseService, times(1)).atualizarExercicio(exerciseId, exerciseDTO);
  }

  @Test
  void deletarExercicio_Success() {
    Long exerciseId = 1L;
    doNothing().when(exerciseService).deletarExercicio(exerciseId);

    ResponseEntity<Void> response = exerciseController.deletarExercicio(exerciseId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(exerciseService, times(1)).deletarExercicio(exerciseId);
  }

  @Test
  void atualizarExercicio_NotFound() {
    Long exerciseId = 1L;
    when(exerciseService.atualizarExercicio(exerciseId, exerciseDTO)).thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<ExerciseDTO> response = exerciseController.atualizarExercicio(exerciseId, exerciseDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(exerciseService, times(1)).atualizarExercicio(exerciseId, exerciseDTO);
  }

  @Test
  void deletarExercicio_NotFound() {
    Long exerciseId = 1L;
    doThrow(new ResourceNotFoundException(null)).when(exerciseService).deletarExercicio(exerciseId);

    ResponseEntity<Void> response = exerciseController.deletarExercicio(exerciseId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(exerciseService, times(1)).deletarExercicio(exerciseId);
  }

  @Test
  void criarExercicio_ShouldReturnCreatedExercise() {
    when(exerciseService.criarExercicio(any(ExerciseDTO.class))).thenReturn(exerciseDTO);

    ResponseEntity<ExerciseDTO> response = exerciseController.criarExercicio(exerciseDTO);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(exerciseDTO, response.getBody());
  }

  @Test
  void criarExercicio_ShouldReturnNotFound() {
    when(exerciseService.criarExercicio(any(ExerciseDTO.class))).thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<ExerciseDTO> response = exerciseController.criarExercicio(exerciseDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void criarExercicio_ShouldReturnInternalServerError() {
    when(exerciseService.criarExercicio(any(ExerciseDTO.class))).thenThrow(new RuntimeException());

    ResponseEntity<ExerciseDTO> response = exerciseController.criarExercicio(exerciseDTO);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void listarExercicios_ShouldReturnListOfExercises() {
    when(exerciseService.listarExercicios()).thenReturn(Collections.singletonList(exerciseDTO));

    ResponseEntity<List<ExerciseDTO>> response = exerciseController.listarExercicios();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void listarExercicios_ShouldReturnNotFound() {
    when(exerciseService.listarExercicios()).thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<List<ExerciseDTO>> response = exerciseController.listarExercicios();

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void listarExercicios_ShouldReturnInternalServerError() {
    when(exerciseService.listarExercicios()).thenThrow(new RuntimeException());

    ResponseEntity<List<ExerciseDTO>> response = exerciseController.listarExercicios();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void atualizarExercicio_ShouldReturnUpdatedExercise() {
    when(exerciseService.atualizarExercicio(anyLong(), any(ExerciseDTO.class))).thenReturn(exerciseDTO);

    ResponseEntity<ExerciseDTO> response = exerciseController.atualizarExercicio(1L, exerciseDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(exerciseDTO, response.getBody());
  }

  @Test
  void atualizarExercicio_ShouldReturnNotFound() {
    when(exerciseService.atualizarExercicio(anyLong(), any(ExerciseDTO.class)))
        .thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<ExerciseDTO> response = exerciseController.atualizarExercicio(1L, exerciseDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void atualizarExercicio_ShouldReturnInternalServerError() {
    when(exerciseService.atualizarExercicio(anyLong(), any(ExerciseDTO.class))).thenThrow(new RuntimeException());

    ResponseEntity<ExerciseDTO> response = exerciseController.atualizarExercicio(1L, exerciseDTO);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals(null, response.getBody());
  }

  @Test
  void deletarExercicio_ShouldReturnNoContent() {
    doNothing().when(exerciseService).deletarExercicio(anyLong());

    ResponseEntity<Void> response = exerciseController.deletarExercicio(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  void deletarExercicio_ShouldReturnNotFound() {
    doThrow(new ResourceNotFoundException(null)).when(exerciseService).deletarExercicio(anyLong());

    ResponseEntity<Void> response = exerciseController.deletarExercicio(1L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void deletarExercicio_ShouldReturnInternalServerError() {
    doThrow(new RuntimeException()).when(exerciseService).deletarExercicio(anyLong());

    ResponseEntity<Void> response = exerciseController.deletarExercicio(1L);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
