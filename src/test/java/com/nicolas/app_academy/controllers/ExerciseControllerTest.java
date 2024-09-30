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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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
}
