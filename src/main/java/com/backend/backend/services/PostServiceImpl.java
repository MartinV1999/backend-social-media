package com.backend.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.Post;
import com.backend.backend.models.entities.dto.PostDto;
import com.backend.backend.models.entities.dto.mapper.DtoMapperPost;
import com.backend.backend.repositories.PostRepository;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostRepository postRepository;

  @Override
  public List<PostDto> findAll() {
    List<Post> posts = (List<Post>) postRepository.findAll();
    
    return posts
      .stream()
      .map(p -> DtoMapperPost.builder().setPost(p).build())
      .collect(Collectors.toList());
  }

  @Override
  public PostDto save(Post post) {
    return DtoMapperPost.builder().setPost(postRepository.save(post)).build();
  }

  @Override
  public Optional<PostDto> update(Post post, Long id) {
    Optional<Post> o = postRepository.findById(id);
    Post postOptional = null;
    if(o.isPresent()){
      Post postDb = o.orElseThrow();
      postDb.setDescription(post.getDescription());
      postDb.setImages(post.getImages());
      postDb.setUser(post.getUser());
      postDb.setVotes(post.getVotes());
      postOptional = postRepository.save(postDb);
    }
    return Optional.ofNullable(DtoMapperPost.builder().setPost(postOptional).build());
  }

  @Override
  public Optional<Post> findById(Long id) {
    return postRepository.findById(id);
  }

  @Override
  public Optional<PostDto> showPostById(Long id) {
    return postRepository.findById(id)
      .map(p -> DtoMapperPost
        .builder()
        .setPost(p)
        .build()
      );
  }
  
}
