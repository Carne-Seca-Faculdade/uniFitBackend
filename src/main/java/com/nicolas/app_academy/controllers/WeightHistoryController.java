package com.nicolas.app_academy.controllers;

import com.nicolas.app_academy.dto.WeightHistoryDTO;
import com.nicolas.app_academy.entities.WeightHistory;
import com.nicolas.app_academy.repositories.WeightHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("weight-history")
public class WeightHistoryController {

  @Autowired
  private WeightHistoryRepository weightHistoryRepository;

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<WeightHistoryDTO>> getWeightHistoryByUserId(@PathVariable Long userId) {
    List<WeightHistory> weightHistories = weightHistoryRepository.findByUserId(userId);
    List<WeightHistoryDTO> weightHistoryDTOs = weightHistories.stream()
        .map(WeightHistoryDTO::new)
        .collect(Collectors.toList());

    return ResponseEntity.ok(weightHistoryDTOs);
  }
}
