package com.nicolas.app_academy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nicolas.app_academy.entities.User;

public interface LoginRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
