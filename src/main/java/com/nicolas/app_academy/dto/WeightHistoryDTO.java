package com.nicolas.app_academy.dto;

import com.nicolas.app_academy.entities.WeightHistory;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WeightHistoryDTO {
  private double peso;
  private LocalDate dataRegistro;

  public WeightHistoryDTO(WeightHistory weightHistory) {
    this.peso = weightHistory.getPeso();
    this.dataRegistro = weightHistory.getDataRegistro();
  }
}
