package com.nicolas.app_academy.auth.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nicolas.app_academy.auth.Login;
import com.nicolas.app_academy.auth.services.LoginService;
import com.nicolas.app_academy.auth.services.RegisterService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

  @Autowired
  private LoginService loginService;

  @Autowired
  private RegisterService registerService;

  @PostMapping("/login")
  public ResponseEntity<String> logar(@RequestBody Login login) {
    try {
      return ResponseEntity.ok(loginService.logar(login));
    } catch (AuthenticationException ex) {
      System.out.println(ex.getMessage());
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody Login login) {
    try {
      registerService.registerUser(login);
      return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    } catch (Exception e) {
      return new ResponseEntity<>(
          Map.of("error", "Error to register user"),
          HttpStatus.BAD_REQUEST);
    }
  }
}
