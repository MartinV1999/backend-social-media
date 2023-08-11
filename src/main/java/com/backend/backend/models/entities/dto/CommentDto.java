package com.backend.backend.models.entities.dto;

public class CommentDto {
  
  private Long id;
  //private CommentPostDto post;
  private UserPostDto user;
  private String content;

  public CommentDto(Long id , /* CommentPostDto post,*/  UserPostDto user, String content) {
    this.id = id;
    //this.post = post;
    this.user = user;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /*public CommentPostDto getPost() {
    return post;
  }

  public void setPost(CommentPostDto post) {
    this.post = post;
  }*/

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public UserPostDto getUser() {
    return user;
  }

  public void setUser(UserPostDto user) {
    this.user = user;
  }

}
