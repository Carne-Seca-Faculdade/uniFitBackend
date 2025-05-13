package com.nicolas.app_academy.services.implementations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import com.nicolas.app_academy.components.HttpComponent;
import com.nicolas.app_academy.dto.Login;
import com.nicolas.app_academy.dto.Register;
import com.nicolas.app_academy.services.ILoginService;
import com.nicolas.app_academy.utils.HttpParamsMapBuilder;

@Service
public class LoginServiceKeycloakImpl implements ILoginService<String> {

  @Value("${keycloak.auth-server-url}")
  private String keyCloakServerUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.resource}")
  private String clientId;

  @Value("${keycloak.credentials.secret}")
  private String clientSecret;

  @Value("${keycloak.user-login.grant-type}")
  private String grantType;

  @Value("${base.url}")
  private String baseUrl;

  @Autowired
  private HttpComponent httpComponent;

  @Override
  public ResponseEntity<String> login(Login login) {
    httpComponent.httpHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = HttpParamsMapBuilder.builder()
        .withClient(clientId)
        .withClientSecret(clientSecret)
        .withGrantType(grantType)
        .withUsername(login.email())
        .withPassword(login.password())
        .build();

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpComponent.httpHeaders());

    try {
      ResponseEntity<String> response = httpComponent.restTemplate().postForEntity(
          keyCloakServerUrl + "/protocol/openid-connect/token", request, String.class);
      return ResponseEntity.ok(response.getBody());
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
  }

  @Override
  public ResponseEntity<String> register(Register register) {
    String accessToken = getKeycloakAccessToken();
    String createUserUrl = baseUrl + "/admin/realms/" + realm + "/users";

    Map<String, Object> user = new HashMap<>();
    user.put("username", register.username());
    user.put("email", register.email());
    user.put("enabled", true);
    user.put("emailVerified", true);

    Map<String, Object> credentials = new HashMap<>();
    credentials.put("type", "password");
    credentials.put("value", register.password());
    credentials.put("temporary", false);

    user.put("credentials", Arrays.asList(credentials));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

    try {
      ResponseEntity<String> response = httpComponent.restTemplate().postForEntity(createUserUrl, request,
          String.class);

      return ResponseEntity.ok(response.getBody());
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
  }

  private String getKeycloakAccessToken() {
    String url = keyCloakServerUrl + "/protocol/openid-connect/token";
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("grant_type", "client_credentials");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
      ResponseEntity<Map> response = httpComponent.restTemplate().exchange(url, HttpMethod.POST, request, Map.class);
      return (String) response.getBody().get("access_token");
    } catch (HttpClientErrorException e) {
      throw new RuntimeException("Failed to authenticate with Keycloak", e);
    }
  }
}
