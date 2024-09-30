package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.services.UserService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerIntegrationTest {

  @MockBean
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private UserDTO userDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userDTO = new UserDTO();
    userDTO.setName("John Doe");
    userDTO.setEmail("john.doe@example.com");
  }

  @Test
  void criarUser_ShouldReturnCreatedUser() {
    when(userService.criarUser(any(UserDTO.class))).thenReturn(userDTO);

    ResponseEntity<UserDTO> response = userController.criarUser(userDTO);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("John Doe", response.getBody().getName());
    assertEquals("john.doe@example.com", response.getBody().getEmail());
  }

  @Test
  void listarUsers_ShouldReturnListOfUsers() {
    List<UserDTO> userList = new ArrayList<>();
    userList.add(userDTO);
    when(userService.listarUsers()).thenReturn(userList);

    ResponseEntity<List<UserDTO>> response = userController.listarUsers();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals("John Doe", response.getBody().get(0).getName());
  }

  @Test
  void calcularIMC_ShouldReturnIMC() {
    when(userService.calcularIMC(1L)).thenReturn("25.0");

    ResponseEntity<Map<String, String>> response = userController.calcularIMC(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("25.0", response.getBody().get("IMC"));
  }

  @Test
  void addTrainingPlans_ShouldReturnNoContent() {
    List<Long> trainingPlanIds = List.of(1L, 2L);

    ResponseEntity<String> response = userController.addTrainingPlans(1L, trainingPlanIds);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService).setTrainingPlansForUser(eq(1L), eq(trainingPlanIds));
  }

  @Test
  void atualizarUser_ShouldReturnUpdatedUser() {
    when(userService.atualizarUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

    ResponseEntity<UserDTO> response = userController.atualizarUser(1L, userDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("John Doe", response.getBody().getName());
  }

  @Test
  void deletarUser_ShouldReturnNoContent() {
    ResponseEntity<Void> response = userController.deletarUser(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService).deletarUser(1L);
  }

  @Test
  void criarUser_ShouldReturnNotFound_WhenResourceNotFoundExceptionThrown() {
    when(userService.criarUser(any(UserDTO.class))).thenThrow(new ResourceNotFoundException("Resource not found"));

    ResponseEntity<UserDTO> response = userController.criarUser(userDTO);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void listarUsers_ShouldReturnInternalServerError_WhenExceptionThrown() {
    when(userService.listarUsers()).thenThrow(new RuntimeException("Unexpected error"));

    ResponseEntity<List<UserDTO>> response = userController.listarUsers();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }
}
