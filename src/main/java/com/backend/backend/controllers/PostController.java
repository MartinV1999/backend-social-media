package com.backend.backend.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.models.entities.Comment;
import com.backend.backend.models.entities.Post;
import com.backend.backend.models.entities.PostPictures;
import com.backend.backend.models.entities.Role;
import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.PostDto;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.services.IS3Service;
import com.backend.backend.services.PostPicturesService;
import com.backend.backend.services.PostService;
import com.backend.backend.services.UserService;


@RestController()
@RequestMapping("/posts")
@CrossOrigin(originPatterns = "*")
public class PostController {

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private PostPicturesService postPicturesService;

  @Autowired
  private IS3Service s3Service;

  @GetMapping("/page/{page}")
  public Page<PostDto> list(@PathVariable Integer page){
    Pageable pageable = PageRequest.of(page, 5);
    return postService.findAll(pageable);
  }

  @GetMapping("/page/{page}/{query}")
  public Page<PostDto> search(@PathVariable Integer page, @PathVariable String query){
    Pageable pageable = PageRequest.of(page, 5);
    return postService.findByQuery(query, pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> show(@PathVariable Long id){
    Optional<PostDto> o = postService.showPostById(id);
    if(o.isPresent()){
      return ResponseEntity.ok(o.orElseThrow());
    }else{
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestParam("userId") Long userId, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam(value= "file", required = false) List<MultipartFile> file){
    
    UUID uuid = UUID.randomUUID();
    Post post = new Post();
    List<PostPictures> postPictures = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();

    try {

      Optional<User> userOptional = userService.getUserById(userId);
      if(userOptional.isPresent()){
        post.setUser(userOptional.orElseThrow());
        post.setTitle(title);
        post.setVotes(0);
        post.setDescription(description);
        post.setComments(comments);
        post.setUuid(uuid);

        if(file != null){
          postPictures = createPictures(uuid, post, file);
        }

        post.setImages(postPictures);
        postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestParam("uuid") UUID uuid, @RequestParam(value = "DeleteFiles" , required = false) String[] deleteFiles ,@RequestParam("userId") Long userId, @RequestParam("title") String title, @RequestParam("description") String description, @RequestParam(value = "file", required = false) List<MultipartFile> file){
    
    Post post = new Post();
    List<PostPictures> postPictures = new ArrayList<>();
    List<Comment> comments = new ArrayList<>();
    Optional<Post> postop = postService.findById(id);

    try {

      Optional<User> user = userService.getUserById(userId);
      if((user.isPresent() && postop.orElseThrow().getUser().getId() == userId) || getAdmin(userId)){
        post.setUser(postop.orElseThrow().getUser());

        if(deleteFiles != null && deleteFiles.length > 0){
          for(String filename : deleteFiles){
            Boolean isDelete = s3Service.deleteFile("dev/posts/", filename, uuid);
            if(isDelete){
              postPicturesService.deleteByFilename(filename);
            }
          }
        }
        
        if(file != null){
          postPictures = createPictures(uuid, post, file);
        }
        
        post.setId(id);
        post.setComments(comments);
        post.setTitle(title);
        post.setDescription(description);
        post.setVotes(0);
        post.setImages(postPictures);

      return ResponseEntity.status(HttpStatus.CREATED).body(postService.update(post, id));
      } else {
        throw new Exception("Usuario no tiene permisos");
      }


    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return ResponseEntity.badRequest().build();
  }

  private List<PostPictures> createPictures(UUID uuid, Post post ,List<MultipartFile> file) throws IOException{
    List<PostPictures> postPictures = new ArrayList<>();
    for (MultipartFile multipartFile : file) {
      UUID nameFile = UUID.randomUUID();
      PostPictures postPicture = new PostPictures();
      String originalFileName = multipartFile.getOriginalFilename();
      String[] extensions = {};
      if(originalFileName != null){
        extensions = multipartFile.getOriginalFilename().split("\\."); 
      }

      String filename = nameFile + "." + extensions[1];

      String url = s3Service.uploadFile(filename, uuid, "dev/posts/", multipartFile);

      postPicture.setPost(post);
      postPicture.setUrl(url);
      postPicture.setFilename(filename);
      postPictures.add(postPicture);
    }
    return postPictures;
  }

  // private ResponseEntity<?> validation(BindingResult result) {
  //   Map<String, String> errors = new HashMap<>();
  //   result.getFieldErrors().forEach(err -> {
  //       errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
  //   });
  //   return ResponseEntity.badRequest().body(errors);
  // }

  private boolean getAdmin(Long user){
    Optional<UserDto> op = userService.findById(user);
    if(op.isPresent()){
      UserDto udto = op.orElseThrow();
      for(Role role : udto.getRoles()){
        if(role.getId() == 1){
          System.out.println(role.getName());
          return true;
        }
      }
    }
    return false;
  }

}
