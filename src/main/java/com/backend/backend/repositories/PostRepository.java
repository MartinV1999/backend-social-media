package com.backend.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.Post;

public interface PostRepository extends CrudRepository<Post, Long> {

}
