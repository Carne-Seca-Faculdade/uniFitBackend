package com.nicolas.app_academy.dto;

import java.io.Serializable;

import com.nicolas.app_academy.entities.BodyMeasurements;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BodyMeasurementsDTO implements Serializable {
  private Long id;
  private Float circumferenceArm;
  private Float circumFerenceWaist;
  private Float circumFerenceLegs;

  public BodyMeasurementsDTO(BodyMeasurements bodyMeasurements) {
    this.id = bodyMeasurements.getId();
    this.circumferenceArm = bodyMeasurements.getCircumferenceArm();
    this.circumFerenceWaist = bodyMeasurements.getCircumFerenceWaist();
    this.circumFerenceLegs = bodyMeasurements.getCircumFerenceLegs();
  }
}
