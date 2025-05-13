// package com.nicolas.app_academy.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.nicolas.app_academy.dto.Login;
// import com.nicolas.app_academy.entities.User;
// import com.nicolas.app_academy.repositories.LoginRepository;
// import com.nicolas.app_academy.services.exception.EmailAlreadyInUseException;

// @Service
// public class RegisterService {
// @Autowired
// private LoginRepository loginRepository;

// @Autowired
// private PasswordEncoder passwordEncoder;

// public User registerUser(Login login) {

// if (loginRepository.findByEmail(login.email()).isPresent()) {
// throw new EmailAlreadyInUseException("Este e-mail já está em uso.");
// }
// User newUser = new User();
// newUser.setEmail(login.email());
// newUser.setPassword(passwordEncoder.encode(login.password()));
// newUser.setRole("USER");
// return loginRepository.save(newUser);
// }
// }
