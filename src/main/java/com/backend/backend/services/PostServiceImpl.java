package com.backend.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional(readOnly = true)
  public Page<PostDto> findAll(Pageable pageable) {
    Page<Post> postPage = postRepository.findAll(pageable);
    return postPage.map(u -> DtoMapperPost.builder().setPost(u).build());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostDto> findByQuery( String query, Pageable pageable) {
    Page<Post> postPage = postRepository.findByQuery(query, pageable);
    return postPage.map(u -> DtoMapperPost.builder().setPost(u).build());
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
      postDb.setId(post.getId());
      postDb.setTitle(post.getTitle());
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
