package com.backend.backend.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.Post;


public interface PostRepository extends CrudRepository<Post, Long> {
  Page<Post> findAll(Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.title ILIKE %?1%")
  Page<Post> findByQuery(String query, Pageable pageable);
}
