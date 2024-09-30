package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.ProgressDTO;
import com.nicolas.app_academy.services.ProgressService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

  @Autowired
  private ProgressService progressService;

  @PostMapping
  public ResponseEntity<ProgressDTO> criarProgress(@RequestBody ProgressDTO progressDTO,
      @PathVariable List<Long> userId) {
    try {
      ProgressDTO savedProgress = progressService.criarProgress(progressDTO, userId);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedProgress);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping
  public ResponseEntity<List<ProgressDTO>> listarProgress() {
    try {
      List<ProgressDTO> progressList = progressService.listarProgress();
      return ResponseEntity.ok(progressList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProgressDTO> atualizarProgress(
      @PathVariable Long id,
      @RequestBody ProgressDTO progressDTO) {
    try {
      ProgressDTO updatedProgress = progressService.atualizarProgress(id, progressDTO);
      return ResponseEntity.ok(updatedProgress);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarProgress(@PathVariable Long id) {
    try {
      progressService.deletarProgress(id);
      return ResponseEntity.noContent().build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
