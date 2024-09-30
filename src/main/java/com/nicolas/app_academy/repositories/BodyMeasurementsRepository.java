package com.nicolas.app_academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicolas.app_academy.entities.BodyMeasurements;

public interface BodyMeasurementsRepository extends JpaRepository<BodyMeasurements, Long> {

}
