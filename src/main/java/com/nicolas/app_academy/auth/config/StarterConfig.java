package com.nicolas.app_academy.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.UserRepository;

@Component
public class StarterConfig implements CommandLineRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    User userAdmin = new User();
    userAdmin.setEmail("admin@gmail.com");
    userAdmin.setPassword(passwordEncoder.encode("admin"));
    userAdmin.setName("Ademir");
    userAdmin.setRole("ADMIN");

    userRepository.save(userAdmin);
  }
}
