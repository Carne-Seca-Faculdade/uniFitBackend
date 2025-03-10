package com.nicolas.app_academy.auth.config;

import io.jsonwebtoken.SignatureAlgorithm;

public class JWTKeys {
  public static final String SECRET_KEY = "KKKKKKKKKKKKKKKKJJJJJJJJJJKKKKKJJJJJJKJKJKJSDLKFJSDLKJFLKSF";
  public static final SignatureAlgorithm ALGORITMO_ASSINATURA = SignatureAlgorithm.HS256;
  public static final int HORAS_EXPIRACAO_TOKEN = 1;
}