package com.backend.backend.models.entities.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.backend.backend.models.entities.Post;
import com.backend.backend.models.entities.dto.CommentDto;
import com.backend.backend.models.entities.dto.PostDto;
import com.backend.backend.models.entities.dto.PostPicturesDto;
import com.backend.backend.models.entities.dto.UserPostDto;

public class DtoMapperPost {
  private Post post;

  private DtoMapperPost(){

  }

  public static DtoMapperPost builder(){
    return new DtoMapperPost();
  }

  public DtoMapperPost setPost(Post post){
    this.post = post;
    return this;
  }

  public PostDto build(){
    if(post == null){
      throw new RuntimeException("Debe pasar el entity post!");
    }
      List<CommentDto> comments = this.post.getComments().stream()
        .map(c -> DtoMapperComment.builder().setComment(c).build())
        .collect(Collectors.toList());

      List<PostPicturesDto> postPictures = this.post.getImages().stream()
        .map(i -> DtoMapperPostPictures.builder().setPostPictures(i).build())
        .collect(Collectors.toList());
      

    return new PostDto(this.post.getId(),
      this.post.getTitle(),
      new UserPostDto(
        this.post.getUser().getId(),
        this.post.getUser().getFirstname(),
        this.post.getUser().getLastname(),
        this.post.getUser().getUsername(),
        this.post.getUser().getEmail(),
        this.post.getUser().getUrlImage()
      ),
      postPictures, 
      this.post.getDescription(), 
      this.post.getVotes(),
      comments,
      this.post.getUuid()
    );
  }
}
