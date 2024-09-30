package com.nicolas.app_academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nicolas.app_academy.entities.Progress;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

}
