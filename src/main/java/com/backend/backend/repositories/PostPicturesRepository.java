package com.backend.backend.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.models.entities.PostPictures;

public interface PostPicturesRepository extends CrudRepository< PostPictures , Long> {
  
  @Transactional
  @Modifying
  @Query("DELETE FROM PostPictures p WHERE p.filename = ?1")
  void deleteByFilename(String filename);
}
