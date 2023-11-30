package com.backend.backend.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(originPatterns = "*")
public class AuthController {

  @Value("${google.clientId1}")
  private String clientId1;

  @Value("${google.clientId2}")
  private String clientId2;

  @Autowired
  private UserService userService;

  @Autowired
  private ProviderAccountService accountService;

  @PostMapping("/checktoken")
  public ResponseEntity<?> checkToken(@RequestBody() String token) {
    try {
      if (token != null) {
        token = token.substring(7); // Elimina el prefij
        Claims claims = Jwts.parserBuilder()
          .setSigningKey(SECRET_KEY)
          .build()
          .parseClaimsJws(token)
          .getBody();
        return ResponseEntity.ok().body(true); // El token es válido
      }
      return ResponseEntity.badRequest().body(false); // El token es inválido o ha caducado
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(false); // El token es inválido o ha caducado
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
        user.setIsComplete(false);
        
        ac.setEmail(account.getEmail());
        ac.setName(account.getDisplayName());
        ac.setProvider("Google");
        ac.setUser(user);
        accountService.save(ac);
        return generateToken(account.getEmail(), account.getPicture(), user.getId(),false, false);
      } else if (u.isPresent() && !p.isPresent()){
        Optional<User> userOptional = userService.getUserByEmail(account.getEmail());
        ac.setEmail(account.getEmail());
        ac.setName(account.getDisplayName());
        ac.setProvider("Google");
        ac.setUser(userOptional.orElseThrow());
        accountService.save(ac);
        return generateToken(account.getEmail(), account.getPicture(), userOptional.orElseThrow().getId(),false, userOptional.orElseThrow().getIsComplete());
      }else if (p.isPresent() && u.isPresent()) {
        return generateToken(account.getEmail(), account.getPicture(), u.orElseThrow().getId(),false, u.orElseThrow().getIsComplete());
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
      .setAudience(Arrays.asList(clientId1, clientId2))
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

  private Map<String,Object> generateToken(String email, String photo, Long userId, boolean isAdmin, boolean isComplete){
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
    claims.put("isComplete", isComplete);

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
