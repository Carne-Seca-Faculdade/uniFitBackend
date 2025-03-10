package com.nicolas.app_academy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nicolas.app_academy.entities.Weight;

import jakarta.transaction.Transactional;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Long> {

  @Modifying
  @Transactional
  @Query("DELETE FROM Weight w WHERE w.user.id = :userId")
  void deleteByUserId(@Param("userId") Long userId);
}
