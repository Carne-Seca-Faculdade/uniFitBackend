package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.BodyMeasurementsDTO;
import com.nicolas.app_academy.services.BodyMeasurementsService;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/body-measurements")
public class BodyMeasurementsController {

  @Autowired
  private BodyMeasurementsService bodyMeasurementsService;

  @PostMapping("/save")
  public ResponseEntity<BodyMeasurementsDTO> criarBodyMeasurements(
      @RequestBody BodyMeasurementsDTO bodyMeasurementsDTO, @PathVariable Long userId) {
    try {
      BodyMeasurementsDTO savedBodyMeasurements = bodyMeasurementsService.criarBodyMeasurements(userId,
          bodyMeasurementsDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(savedBodyMeasurements);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping
  public ResponseEntity<List<BodyMeasurementsDTO>> listarBodyMeasurements() {
    try {
      List<BodyMeasurementsDTO> bodyMeasurementsList = bodyMeasurementsService.listarBodyMeasurements();
      return ResponseEntity.ok(bodyMeasurementsList);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<BodyMeasurementsDTO> atualizarBodyMeasurements(
      @PathVariable Long id,
      @RequestBody BodyMeasurementsDTO bodyMeasurementsDTO) {
    try {
      BodyMeasurementsDTO updatedBodyMeasurements = bodyMeasurementsService.atualizarBodyMeasurements(id,
          bodyMeasurementsDTO);
      return ResponseEntity.ok(updatedBodyMeasurements);
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarBodyMeasurements(@PathVariable Long id) {
    try {
      bodyMeasurementsService.deletarBodyMeasurements(id);
      return ResponseEntity.noContent().build();
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
