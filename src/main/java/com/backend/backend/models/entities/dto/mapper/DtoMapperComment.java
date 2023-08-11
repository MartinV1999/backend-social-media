package com.backend.backend.models.entities.dto.mapper;

import com.backend.backend.models.entities.Comment;
import com.backend.backend.models.entities.dto.CommentDto;
import com.backend.backend.models.entities.dto.UserPostDto;

public class DtoMapperComment {
  private Comment comment;

  private DtoMapperComment(){
    
  }

  public static DtoMapperComment builder(){
    return new DtoMapperComment();
  }

  public DtoMapperComment setComment(Comment comment){
    this.comment = comment;
    return this;
  }

  public CommentDto build(){
    if(comment == null){
      throw new RuntimeException("Debe pasar el entity comment!");
    }

      UserPostDto commentUser = new UserPostDto(
        this.comment.getUser().getId(),
        this.comment.getUser().getFirstname(), 
        this.comment.getUser().getLastname(), 
        this.comment.getUser().getUsername(), 
        this.comment.getUser().getEmail()
      );

    return new CommentDto(
      this.comment.getId(),
      commentUser,
      this.comment.getContent() 
    );
  }
}
