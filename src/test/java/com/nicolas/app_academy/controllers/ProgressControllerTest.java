package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.ProgressDTO;
import com.nicolas.app_academy.services.ProgressService;
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
class ProgressControllerTest {

  @Autowired
  private ProgressController progressController;

  @MockBean
  private ProgressService progressService;

  private ProgressDTO progressDTO;

  @BeforeEach
  void setUp() {
    progressDTO = new ProgressDTO();
  }

  @Test
  void criarProgress_Success() {
    List<Long> userId = List.of(1L);
    when(progressService.criarProgress(progressDTO, userId)).thenReturn(progressDTO);

    ResponseEntity<ProgressDTO> response = progressController.criarProgress(progressDTO, userId);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(progressDTO, response.getBody());
    verify(progressService, times(1)).criarProgress(progressDTO, userId);
  }

  @Test
  void listarProgress_Success() {
    List<ProgressDTO> progressList = new ArrayList<>();
    progressList.add(progressDTO);
    when(progressService.listarProgress()).thenReturn(progressList);

    ResponseEntity<List<ProgressDTO>> response = progressController.listarProgress();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(progressList, response.getBody());
    verify(progressService, times(1)).listarProgress();
  }

  @Test
  void atualizarProgress_Success() {
    Long id = 1L;
    when(progressService.atualizarProgress(id, progressDTO)).thenReturn(progressDTO);

    ResponseEntity<ProgressDTO> response = progressController.atualizarProgress(id, progressDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(progressDTO, response.getBody());
    verify(progressService, times(1)).atualizarProgress(id, progressDTO);
  }

  @Test
  void deletarProgress_Success() {
    Long id = 1L;
    doNothing().when(progressService).deletarProgress(id);

    ResponseEntity<Void> response = progressController.deletarProgress(id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(progressService, times(1)).deletarProgress(id);
  }

  @Test
  void atualizarProgress_NotFound() {
    Long id = 1L;
    when(progressService.atualizarProgress(id, progressDTO)).thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<ProgressDTO> response = progressController.atualizarProgress(id, progressDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(progressService, times(1)).atualizarProgress(id, progressDTO);
  }

  @Test
  void deletarProgress_NotFound() {
    Long id = 1L;
    doThrow(new ResourceNotFoundException(null)).when(progressService).deletarProgress(id);

    ResponseEntity<Void> response = progressController.deletarProgress(id);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(progressService, times(1)).deletarProgress(id);
  }
}
