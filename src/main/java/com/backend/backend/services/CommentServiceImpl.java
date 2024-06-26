package com.backend.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.models.entities.Comment;
import com.backend.backend.models.entities.dto.CommentDto;
import com.backend.backend.models.entities.dto.mapper.DtoMapperComment;
import com.backend.backend.repositories.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Override
  public List<CommentDto> findAll() {
    List<Comment> comments = (List<Comment>) commentRepository.findAll();
    return comments
      .stream()
      .map(c -> DtoMapperComment.builder().setComment(c).build())
      .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CommentDto> findByPostId(Long id, Pageable pageable) {
    Page<Comment> commentPage = commentRepository.getCommentsByPostId(id, pageable);
    return commentPage.map(c -> DtoMapperComment.builder().setComment(c).build());
  }

  @Override
  @Transactional
  public CommentDto save(Comment comment) {
    return DtoMapperComment.builder().setComment(commentRepository.save(comment)).build();
  }

  @Override
  public Optional<Comment> update(Comment comment, Long id) {
    Optional<Comment> o = commentRepository.findById(id);
    Comment commentOptional = null;
    if(o.isPresent()){
      Comment commentDb = o.orElseThrow();
      commentDb.setContent(comment.getContent());
      commentOptional = commentRepository.save(commentDb);
    }
    return Optional.ofNullable(commentOptional);
  }

  @Override
  public Optional<Comment> findById(Long id) {
    return commentRepository.findById(id);
  }
  
}
