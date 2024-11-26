package com.nicolas.app_academy.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.nicolas.app_academy.auth.Login;
import com.nicolas.app_academy.auth.repositories.LoginRepository;
import com.nicolas.app_academy.auth.utils.JwtUtils;
import com.nicolas.app_academy.entities.User;

@Service
public class LoginService {

  @Autowired
  private LoginRepository repository;
  @Autowired
  private JwtUtils jwtService;
  @Autowired
  private AuthenticationManager authenticationManager;

  public String logar(Login login) {
    var data = repository.findByEmail(login.email()).get();
    System.out.println(data.getEmail());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            login.email(),
            login.password()));
    User user = repository.findByEmail(login.email()).get();
    String jwtToken = jwtService.generateToken(user);

    return jwtToken;
  }
}
