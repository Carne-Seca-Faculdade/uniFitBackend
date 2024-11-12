package com.nicolas.app_academy.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.nicolas.app_academy.entities.Weight;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeightDTO implements Serializable {
    private double value;
    private LocalDate recordedAt;

    public WeightDTO(Weight weight) {
        this.value = weight.getPeso();
    }
}
