package com.backend.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
