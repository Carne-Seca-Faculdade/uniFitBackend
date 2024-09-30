package com.nicolas.app_academy.controllers;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.services.UserService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/usuarios")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/save")
  public ResponseEntity<UserDTO> criarUser(@RequestBody UserDTO userDTO) {
    try {
      UserDTO createUser = userService.criarUser(userDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> listarUsers() {
    try {
      List<UserDTO> users = userService.listarUsers();
      return ResponseEntity.status(HttpStatus.OK).body(users);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/{userId}/calcularIMC")
  public ResponseEntity<Map<String, String>> calcularIMC(@PathVariable Long userId) {
    try {
      String imc = userService.calcularIMC(userId);
      Map<String, String> response = new HashMap<>();
      response.put("IMC", imc);
      return ResponseEntity.ok(response);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/{userId}/set-plano-treino")
  public ResponseEntity<String> addTrainingPlans(@PathVariable Long userId, @RequestBody List<Long> trainingPlanIds) {
    try {
      if (trainingPlanIds == null || trainingPlanIds.isEmpty()) {
        return ResponseEntity.badRequest().body("Ids dos planos de treino nao podem ser nulos");
      }
      userService.setTrainingPlansForUser(userId, trainingPlanIds);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao encontrado");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/{userId}")
  public ResponseEntity<UserDTO> atualizarUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
    try {
      UserDTO updatedUser = userService.atualizarUser(userId, userDTO);
      return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deletarUser(@PathVariable Long userId) {
    try {
      userService.deletarUser(userId);
      return ResponseEntity.noContent().build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
