package com.nicolas.app_academy.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nicolas.app_academy.auth.Login;
import com.nicolas.app_academy.auth.repositories.LoginRepository;
import com.nicolas.app_academy.entities.User;

@Service
public class RegisterService {
  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User registerUser(Login login) {
    User newUser = new User();
    newUser.setEmail(login.email());
    newUser.setPassword(passwordEncoder.encode(login.password()));
    newUser.setRole("USER");
    return loginRepository.save(newUser);
  }
}
