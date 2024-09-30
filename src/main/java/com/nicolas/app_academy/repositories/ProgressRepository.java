package com.nicolas.app_academy.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nicolas.app_academy.entities.Progress;
import com.nicolas.app_academy.entities.User;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
  List<Progress> findByUsersAndMonitoringStartedAt(List<User> user, LocalDate monitoringStartedAt);

}
