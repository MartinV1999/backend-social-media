package com.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.backend.models.entities.Comment;
import com.backend.backend.models.entities.dto.CommentDto;

public interface CommentService {
  List<CommentDto> findAll();

  CommentDto save(Comment comment);

  Optional<Comment> update(Comment comment, Long id);

  Optional<Comment> findById(Long id);

  Page<CommentDto> findByPostId(Long id, Pageable pageable);

}
