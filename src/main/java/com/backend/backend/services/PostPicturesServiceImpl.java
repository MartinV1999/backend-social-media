package com.backend.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.PostPictures;
import com.backend.backend.repositories.PostPicturesRepository;

@Service
public class PostPicturesServiceImpl implements PostPicturesService {

  @Autowired
  private PostPicturesRepository postPicturesRepository;

  @Override
  public PostPictures save(PostPictures postPictures) {
    return postPicturesRepository.save(postPictures);
  }

  @Override
  public void deleteByFilename(String filename) {
    postPicturesRepository.deleteByFilename(filename);
  }
  
}
