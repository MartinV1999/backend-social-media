package com.backend.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

  @Query("SELECT c FROM Comment c WHERE c.post.id = ?1")
  Page<Comment> getCommentsByPostId(Long id, Pageable pageable);

}
