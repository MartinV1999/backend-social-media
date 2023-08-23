  package com.backend.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.models.entities.Post;
import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.PostDto;
import com.backend.backend.models.entities.request.CrearPostRequest;
import com.backend.backend.services.PostService;
import com.backend.backend.services.UserService;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("/posts")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @GetMapping
  public List<PostDto> list(){
    return postService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> show(@PathVariable Long id){
    Optional<Post> o = postService.findById(id);
    if(o.isPresent()){
      return ResponseEntity.ok(o.orElseThrow());
    }else{
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody CrearPostRequest postRequest, BindingResult result){
    if(result.hasErrors()){
      return validation(result);
    }else{
      Optional<User> userOptional = userService.getUserById(postRequest.getUserId());
      if(userOptional.isPresent()){
        Post post = postRequest.getPost();
        post.setUser(userOptional.orElseThrow());
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequest.getPost()));
      }else{
        return ResponseEntity.notFound().build();
      }
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CrearPostRequest postRequest){

    Post post = null;
    Optional<User> userOptional = userService.getUserById(postRequest.getUserId());
    if(!userOptional.isPresent()){
      return ResponseEntity.notFound().build();
    }

    post = postRequest.getPost();
    post.setUser(userOptional.orElseThrow());

    Optional<PostDto> o = postService.update(post , id);
    if(o.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
    }

    return ResponseEntity.notFound().build();
  }

  private ResponseEntity<?> validation(BindingResult result) {
    Map<String, String> errors = new HashMap<>();
    result.getFieldErrors().forEach(err -> {
        errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
  }
}
