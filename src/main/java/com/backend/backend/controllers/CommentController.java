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

import com.backend.backend.models.entities.Comment;
import com.backend.backend.models.entities.Post;
import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.CommentDto;
import com.backend.backend.models.entities.request.CommentRequest;
import com.backend.backend.services.CommentService;
import com.backend.backend.services.PostService;
import com.backend.backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {
  
  @Autowired
  private CommentService commentService;

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @GetMapping
  public List<CommentDto> list(){
    return commentService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> show(@PathVariable Long id){
    Optional<Comment> o = commentService.findById(id);
    if(o.isPresent()){
      return ResponseEntity.ok(o.orElseThrow());
    }else{
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody CommentRequest commentRequest, BindingResult result){
    
    if(result.hasErrors()){
      return validation(result);
    }
    Optional<User> userOptional = userService.getUserById(commentRequest.getUserId());
    Optional<Post> postOptional = postService.findById(commentRequest.getPostId());
    if(postOptional.isPresent() && userOptional.isPresent()){
      Comment comment = null;
      User user = null;
      Post post = postOptional.orElseThrow();
      comment = commentRequest.getComment();
      user = userOptional.orElseThrow();
      comment.setPost(post);
      comment.setUser(user);
      return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(comment));
    }else{
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Comment comment){
    Optional<Comment> o = commentService.update(comment, id);
    if(o.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(o.orElseThrow());
    }

    return ResponseEntity.notFound().build();
  }

  public ResponseEntity<?> validation(BindingResult result){
    Map<String, String> errors = new HashMap<>();
    result.getFieldErrors().forEach(err -> {
      errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
    });

    return ResponseEntity.badRequest().body(errors);
  }
}
