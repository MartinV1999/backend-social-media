package com.backend.backend.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.models.entities.ProvidersAccounts;
import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.models.entities.dto.mapper.DtoMapperUser;
import com.backend.backend.models.entities.request.UserGoogleRequest;
import com.backend.backend.services.ProviderAccountService;
import com.backend.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import static com.backend.backend.auth.TokenJwtConfig.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @Autowired
  private ProviderAccountService accountService;

  @PostMapping("/test")
  public ResponseEntity<?> GoogleAuthTest(@RequestBody UserGoogleRequest account){
    if(validateGoogleToken(account.getIdOAuthToken())){
      System.out.println("Valido");
      return ResponseEntity.ok().build();
    }else{
      System.out.println("Invalido");
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping()
  public Map<String,Object> auth(@RequestBody UserGoogleRequest account){
    ProvidersAccounts ac = new ProvidersAccounts();
    User user = new User();
    if(validateGoogleToken(account.getIdOAuthToken())){
      Optional<ProvidersAccounts> p = accountService.getAccountByEmail(account.getEmail());
      Optional<UserDto> u = userService.getUserByEmail(account.getEmail()).map(us -> DtoMapperUser
        .builder()
        .setUser(us)
        .build());
      if(!p.isPresent() && !u.isPresent()){
        userService.setUserNative(account.getDisplayName(), account.getEmail(), account.getPicture(), 0, "J3Q$8MCU5i&aZnfF");
        Optional<UserDto> userOptional = userService.getUserByEmailDto(account.getEmail());
        user.setId(userOptional.orElseThrow().getId());
        user.setEmail(userOptional.orElseThrow().getEmail());
        user.setUsername(userOptional.orElseThrow().getUsername());
        user.setUrlImage(userOptional.orElseThrow().getUrlImage());
        user.setIsActive(0);
        
        ac.setEmail(account.getEmail());
        ac.setName(account.getDisplayName());
        ac.setProvider("Google");
        ac.setUser(user);
        accountService.save(ac);
        return generateToken(account.getEmail(), account.getPicture(), user.getId(),false);
      } else if (u.isPresent() && !p.isPresent()){
        Optional<User> userOptional = userService.getUserByEmail(account.getEmail());
        ac.setEmail(account.getEmail());
        ac.setName(account.getDisplayName());
        ac.setProvider("Google");
        ac.setUser(userOptional.orElseThrow());
        accountService.save(ac);
        return generateToken(account.getEmail(), account.getPicture(), userOptional.orElseThrow().getId(),false);
      }else if (p.isPresent() && u.isPresent()) {
        return generateToken(account.getEmail(), account.getPicture(), u.orElseThrow().getId(),false);
      }
    }else{
      return null;
    }
    return null;
  }

  private Boolean validateGoogleToken(String token){

    NetHttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new GsonFactory();

    try {
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
      // Specify the CLIENT_ID of the app that accesses the backend:
      // .setAudience(Collections.singletonList("161955849471-bguqectccp35mjt0lmba3k3bko5bd2ur.apps.googleusercontent.com"))
      // Or, if multiple clients access the backend:
      .setAudience(Arrays.asList(
        "161955849471-f6gbmsebsd4q9m9700o8v5ni06gagpr4.apps.googleusercontent.com",
        "161955849471-bguqectccp35mjt0lmba3k3bko5bd2ur.apps.googleusercontent.com"
      ))
      .build();

      // (Receive idTokenString by HTTPS POST)
      GoogleIdToken idToken = verifier.verify(token);

      if (idToken != null) {
        return true;
      } else {
        System.out.println("Invalid ID token.");
        return false;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  private Map<String,Object> generateToken(String email, String photo, Long userId, boolean isAdmin){
    Claims claims = Jwts.claims();
    
    ObjectMapper objectMapper = new ObjectMapper();

    Collection<? extends GrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    try {
     claims.put("authorities", objectMapper.writeValueAsString(roles)); 
    } catch (Exception e) {
      System.out.println(e);
    }
    
    claims.put("isAdmin", isAdmin);
    claims.put("userId", userId);
    claims.put("photo", photo);

    String token = Jwts.builder()
      .setClaims(claims)
      .setSubject(email)
      .signWith(SECRET_KEY)
      .setIssuedAt(new Date())
      // .setExpiration(new Date(System.currentTimeMillis() + 3600000))
      .compact();

    Map<String,Object> body = new HashMap<>();
    body.put("token", token);
    body.put("message", String.format("Hola %s, has iniciado sesion con exito", email));
    body.put("username", email);
    return body;
  }

}
