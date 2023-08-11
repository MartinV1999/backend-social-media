package com.backend.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.models.entities.request.UserRequest;
import com.backend.backend.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserController {
  
  @Autowired
  private UserService userService;

  @GetMapping
  public List<UserDto> list(){
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> show(@PathVariable Long id){
    Optional<UserDto> userOptional = userService.findById(id);

    if(userOptional.isPresent()){
      return ResponseEntity.ok(userOptional.orElseThrow());
    }else{
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody User user){
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequest user){
    Optional<UserDto> o = userService.update(user, id);
    if(o.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

}
