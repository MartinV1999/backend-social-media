package com.backend.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

  @GetMapping("/page/{page}")
  public Page<UserDto> list(@PathVariable Integer page){
    Pageable pageable = PageRequest.of(page, 15);
    return userService.findAll(pageable);
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
    Optional<User> o = userService.getUserByEmail(user.getEmail());
    if(!o.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }else{
      return validationNotDuplicateEmail();
    }

  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserRequest user){
    Optional<UserDto> o = userService.update(user, id);
    if(o.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> remove(@PathVariable Long id){
    Optional<User> u = userService.getUserById(id);
    if(u.isPresent()){
      userService.removeUser(id);
      return ResponseEntity.ok().build();
    }else{
      return ResponseEntity.notFound().build();
    }
  }

  private ResponseEntity<?> validationNotDuplicateEmail(){
    Map<String, String> errors = new HashMap<>();
    errors.put("duplicate","El correo electronico esta en uso!");
    return ResponseEntity.badRequest().body(errors);
  }
}
