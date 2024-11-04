package com.nicolas.app_academy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicolas.app_academy.entities.WeightHistory;

public interface WeightHistoryRepository extends JpaRepository<WeightHistory, Long> {
  List<WeightHistory> findByUserId(Long userId);

}
