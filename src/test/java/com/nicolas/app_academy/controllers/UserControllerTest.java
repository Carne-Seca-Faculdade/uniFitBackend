package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.services.UserService;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class UserControllerTest {

  @Autowired
  private UserController userController;

  @MockBean
  private UserService userService;

  private UserDTO userDTO;

  @BeforeEach
  void setUp() {
    userDTO = new UserDTO();
    userDTO.setName("Test User");
  }

  @Test
  void criarUser_Success() {
    when(userService.criarUser(userDTO)).thenReturn(userDTO);

    ResponseEntity<UserDTO> response = userController.criarUser(userDTO);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(userDTO, response.getBody());
    verify(userService, times(1)).criarUser(userDTO);
  }

  @Test
  void listarUsers_Success() {
    List<UserDTO> users = new ArrayList<>();
    users.add(userDTO);
    when(userService.listarUsers()).thenReturn(users);

    ResponseEntity<List<UserDTO>> response = userController.listarUsers();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(users, response.getBody());
    verify(userService, times(1)).listarUsers();
  }

  @Test
  void calcularIMC_Success() {
    Long userId = 1L;
    String imc = "22.5";
    when(userService.calcularIMC(userId)).thenReturn(imc);

    ResponseEntity<Map<String, String>> response = userController.calcularIMC(userId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("22.5", response.getBody().get("IMC"));
    verify(userService, times(1)).calcularIMC(userId);
  }

  @Test
  void addTrainingPlans_Success() {
    Long userId = 1L;
    List<Long> trainingPlanIds = List.of(1L, 2L);
    doNothing().when(userService).setTrainingPlansForUser(userId, trainingPlanIds);

    ResponseEntity<String> response = userController.addTrainingPlans(userId, trainingPlanIds);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService, times(1)).setTrainingPlansForUser(userId, trainingPlanIds);
  }

  @Test
  void addTrainingPlans_BadRequest() {
    Long userId = 1L;
    ResponseEntity<String> response = userController.addTrainingPlans(userId, null);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Ids dos planos de treino nao podem ser nulos", response.getBody());
  }

  @Test
  void atualizarUser_Success() {
    Long userId = 1L;
    when(userService.atualizarUser(userId, userDTO)).thenReturn(userDTO);

    ResponseEntity<UserDTO> response = userController.atualizarUser(userId, userDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDTO, response.getBody());
    verify(userService, times(1)).atualizarUser(userId, userDTO);
  }

  @Test
  void deletarUser_Success() {
    Long userId = 1L;
    doNothing().when(userService).deletarUser(userId);

    ResponseEntity<Void> response = userController.deletarUser(userId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService, times(1)).deletarUser(userId);
  }

  @Test
  void criarUser_NotFound() {
    when(userService.criarUser(userDTO)).thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<UserDTO> response = userController.criarUser(userDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(userService, times(1)).criarUser(userDTO);
  }

  @Test
  void atualizarUser_NotFound() {
    Long userId = 1L;
    when(userService.atualizarUser(userId, userDTO)).thenThrow(new ResourceNotFoundException(null));

    ResponseEntity<UserDTO> response = userController.atualizarUser(userId, userDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(userService, times(1)).atualizarUser(userId, userDTO);
  }

  @Test
  void deletarUser_NotFound() {
    Long userId = 1L;
    doThrow(new ResourceNotFoundException(null)).when(userService).deletarUser(userId);

    ResponseEntity<Void> response = userController.deletarUser(userId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(userService, times(1)).deletarUser(userId);
  }
}
