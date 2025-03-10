package com.nicolas.app_academy.controllers;

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

import com.nicolas.app_academy.dto.Login;
import com.nicolas.app_academy.services.LoginService;
import com.nicolas.app_academy.services.RegisterService;
import com.nicolas.app_academy.services.exception.EmailAlreadyInUseException;

@RestController
@RequestMapping("/auth")
@CrossOrigin({ "*" })
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
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody Login login) {
    try {
      registerService.registerUser(login);
      return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    } catch (EmailAlreadyInUseException e) {
      return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception e) { 
      return new ResponseEntity<>(Map.of("error", "Error to register user"), HttpStatus.BAD_REQUEST);
    }
  }
}
