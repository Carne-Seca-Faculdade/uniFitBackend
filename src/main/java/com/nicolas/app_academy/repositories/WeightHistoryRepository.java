package com.nicolas.app_academy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nicolas.app_academy.entities.WeightHistory;

import jakarta.transaction.Transactional;

public interface WeightHistoryRepository extends JpaRepository<WeightHistory, Long> {
  List<WeightHistory> findByUserId(Long userId);

  @Modifying
  @Transactional
  @Query("DELETE FROM WeightHistory w WHERE w.user.id = :userId")
  void deleteByUserId(@Param("userId") Long userId);

}
