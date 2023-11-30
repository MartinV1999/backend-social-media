package com.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.Post;
import com.backend.backend.models.entities.dto.PostDto;

@Service
public interface PostService {

  List<PostDto> findAll();

  Page<PostDto> findAll(Pageable pageable);

  Page<PostDto> findByQuery(String query, Pageable pageable);

  PostDto save(Post post);

  Optional<PostDto> update(Post post, Long id);

  Optional<Post> findById(Long id);

  Optional<PostDto> showPostById(Long id);
}
