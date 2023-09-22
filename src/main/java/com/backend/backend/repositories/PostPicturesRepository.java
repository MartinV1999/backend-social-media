package com.backend.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.PostPictures;

public interface PostPicturesRepository extends CrudRepository< PostPictures , Long> {
  
}
