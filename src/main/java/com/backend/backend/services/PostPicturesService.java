package com.backend.backend.services;

import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.PostPictures;

@Service
public interface PostPicturesService {
  PostPictures save(PostPictures postPictures);
}
