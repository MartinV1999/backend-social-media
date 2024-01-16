package com.backend.backend.models.entities.dto;

import java.util.Date;

public class CommentDto {
  
  private Long id;
  private UserPostDto user;
  private String content;
  private Date createAt;

  public CommentDto(Long id , UserPostDto user, String content, Date createAt) {
    this.id = id;
    this.user = user;
    this.content = content;
    this.createAt = createAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

}
