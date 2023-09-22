package com.backend.backend.models.entities.dto.mapper;

import com.backend.backend.models.entities.PostPictures;
import com.backend.backend.models.entities.dto.PostPicturesDto;

public class DtoMapperPostPictures {
  private PostPictures pictures;

  private DtoMapperPostPictures(){

  }

  public static DtoMapperPostPictures builder(){
    return new DtoMapperPostPictures();
  }

  public DtoMapperPostPictures setPostPictures(PostPictures postPictures){
    this.pictures = postPictures;
    return this;
  }

  public PostPicturesDto build () {
    if(pictures == null) {
      throw new RuntimeException("Debe pasar el entity PostPictures");
    }

    return new PostPicturesDto(
      this.pictures.getId(),
      this.pictures.getUrl(),
      this.pictures.getFilename(),
      this.pictures.getUuid()
    );
  }
}
