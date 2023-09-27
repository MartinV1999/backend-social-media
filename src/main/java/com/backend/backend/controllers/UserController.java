package com.backend.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.models.entities.request.UserRequest;
import com.backend.backend.services.IS3Service;
import com.backend.backend.services.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(originPatterns = "*")
public class UserController {
  
  @Autowired
  private UserService userService;

  @Autowired
  private IS3Service s3Service;

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

  @PostMapping("/test")
  public ResponseEntity<?> test(@RequestPart("user") User user, @RequestParam("file") MultipartFile file){
    return ResponseEntity.status(HttpStatus.CREATED).body(file.getOriginalFilename());
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestPart("user") User user, @RequestParam(value = "file", required = false) MultipartFile file){
    
    try {
      Optional<User> o = userService.getUserByEmail(user.getEmail());
      UUID uuid = UUID.randomUUID();
      if(!o.isPresent()){
        // User u = new User();
        // u.setFirstname(user.getFirstname());
        // u.setLastname(user.getLastname());
        // u.setUsername(user.getUsername());
        // u.setEmail(user.getEmail());
        // u.setRut(user.getRut());
        // u.setIdentificator(user.getIdentificator());
        // u.setBirthday(user.getBirthday());
        // u.setAddress(user.getAddress());
        // u.setPassword(user.getPassword());
        // u.setAdmin(user.isAdmin());
        UUID uuid2 = UUID.randomUUID();
        String[] extension = file.getOriginalFilename().split("\\."); 
        String filename = uuid + "." + extension[1];
        String url = s3Service.uploadFile(filename, uuid2, "dev/users/", file);
        user.setUrlImage(url);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
      }else{
        return validationNotDuplicateEmail();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
