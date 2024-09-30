package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.BodyMeasurementsDTO;
import com.nicolas.app_academy.services.BodyMeasurementsService;
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
class BodyMeasurementsControllerTest {

  @Autowired
  private BodyMeasurementsController bodyMeasurementsController;

  @MockBean
  private BodyMeasurementsService bodyMeasurementsService;

  private BodyMeasurementsDTO bodyMeasurementsDTO;

  @BeforeEach
  void setUp() {
    bodyMeasurementsDTO = new BodyMeasurementsDTO();
  }

  @Test
  void criarBodyMeasurements_Success() {
    Long userId = 1L;
    when(bodyMeasurementsService.criarBodyMeasurements(userId, bodyMeasurementsDTO)).thenReturn(bodyMeasurementsDTO);

    ResponseEntity<BodyMeasurementsDTO> response = bodyMeasurementsController.criarBodyMeasurements(bodyMeasurementsDTO,
        userId);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(bodyMeasurementsDTO, response.getBody());
    verify(bodyMeasurementsService, times(1)).criarBodyMeasurements(userId, bodyMeasurementsDTO);
  }

  @Test
  void listarBodyMeasurements_Success() {
    List<BodyMeasurementsDTO> bodyMeasurementsList = new ArrayList<>();
    bodyMeasurementsList.add(bodyMeasurementsDTO);
    when(bodyMeasurementsService.listarBodyMeasurements()).thenReturn(bodyMeasurementsList);

    ResponseEntity<List<BodyMeasurementsDTO>> response = bodyMeasurementsController.listarBodyMeasurements();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(bodyMeasurementsList, response.getBody());
    verify(bodyMeasurementsService, times(1)).listarBodyMeasurements();
  }

  @Test
  void atualizarBodyMeasurements_Success() {
    Long id = 1L;
    when(bodyMeasurementsService.atualizarBodyMeasurements(id, bodyMeasurementsDTO)).thenReturn(bodyMeasurementsDTO);

    ResponseEntity<BodyMeasurementsDTO> response = bodyMeasurementsController.atualizarBodyMeasurements(id,
        bodyMeasurementsDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(bodyMeasurementsDTO, response.getBody());
    verify(bodyMeasurementsService, times(1)).atualizarBodyMeasurements(id, bodyMeasurementsDTO);
  }

  @Test
  void deletarBodyMeasurements_Success() {
    Long id = 1L;
    doNothing().when(bodyMeasurementsService).deletarBodyMeasurements(id);

    ResponseEntity<Void> response = bodyMeasurementsController.deletarBodyMeasurements(id);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(bodyMeasurementsService, times(1)).deletarBodyMeasurements(id);
  }

  @Test
  void atualizarBodyMeasurements_NotFound() {
    Long id = 1L;
    when(bodyMeasurementsService.atualizarBodyMeasurements(id, bodyMeasurementsDTO))
        .thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<BodyMeasurementsDTO> response = bodyMeasurementsController.atualizarBodyMeasurements(id,
        bodyMeasurementsDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(bodyMeasurementsService, times(1)).atualizarBodyMeasurements(id, bodyMeasurementsDTO);
  }

  @Test
  void deletarBodyMeasurements_NotFound() {
    Long id = 1L;
    doThrow(new ResourceNotFoundException(null)).when(bodyMeasurementsService).deletarBodyMeasurements(id);

    ResponseEntity<Void> response = bodyMeasurementsController.deletarBodyMeasurements(id);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(bodyMeasurementsService, times(1)).deletarBodyMeasurements(id);
  }
}
