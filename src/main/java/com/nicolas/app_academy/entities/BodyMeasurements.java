package com.nicolas.app_academy.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class BodyMeasurements {
  private Float circumferenceArm;
  private Float circumFerenceWaist;
  private Float circumFerenceLegs;
}
