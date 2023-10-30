package com.backend.backend.controllers;

import java.io.IOException;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import jakarta.validation.Valid;

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

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestPart("user") User user, BindingResult result, @RequestParam(value = "file", required = false) MultipartFile file){
    
    if(result.hasErrors()){
      return validationHasErrors(result);
    }

    try {
      Optional<User> o = userService.getUserByEmail(user.getEmail());
      UUID uuid = UUID.randomUUID();
      if(!o.isPresent()){
        if(file != null){
          UUID uuid2 = UUID.randomUUID();
          String[] extension = file.getOriginalFilename().split("\\."); 
          String filename = uuid2 + "." + extension[1];
          String url = s3Service.uploadFile(filename, uuid, "dev/users/", file);
          user.setUrlImage(url);
          user.setFilename(filename);
          user.setUuid(uuid);
        }
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
  public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestPart("user") UserRequest user, BindingResult result, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "DeleteFile", required = false) String deleteFile) throws IOException{

    if(result.hasErrors()){
      return validationHasErrors(result);
    }
    Optional<UserDto> o = userService.update(user, id, file, (deleteFile != null ? true : false));
    if(o.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  // @PutMapping("/completeAccount/{id}")
  // public ResponseEntity<?> completeAccountData(@PathVariable Long id, @RequestPart("user") UserRequest user, @RequestParam(value = "file", required = false) MultipartFile file){
    
  //   Optional<User> op = userService.getUserById(id);
  //   User userDb = op.orElseThrow();
  //   user.setEmail(userDb.getEmail());
  //   user.setUsername(userDb.getUsername());
  //   user.setIsComplete(true);
  //   Optional<UserDto> o = userService.update(user, id, file);
  //   if(o.isPresent()){
  //     return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
  //   }
  //   return ResponseEntity.notFound().build();
  // }

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

  private ResponseEntity<?> validationHasErrors(BindingResult result){
    Map<String, String> errors = new HashMap<>();
    for(FieldError error : result.getFieldErrors()){
      errors.put(error.getField(),error.getDefaultMessage());
    }
    return ResponseEntity.badRequest().body(errors);
  }

}
