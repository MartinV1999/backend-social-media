package com.backend.backend.models.entities.dto;

import java.util.List;

public class PostDto {
  private Long id;
  private String title;
  private UserPostDto user;
  private String urlMedia;
  private String description;
  private Integer votes;
  private List<CommentDto> comments;
  
  public PostDto(Long id, String title, UserPostDto user, String urlMedia, String description, Integer votes, List<CommentDto> comments) {
    this.id = id;
    this.title = title;
    this.user = user;
    this.urlMedia = urlMedia;
    this.description = description;
    this.votes = votes;
    this.comments = comments;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserPostDto getUser() {
    return user;
  }

  public void setUser(UserPostDto user) {
    this.user = user;
  }

  public List<CommentDto> getComments() {
    return comments;
  }

  public void setComments(List<CommentDto> comments) {
    this.comments = comments;
  }

  public String getUrlMedia() {
    return urlMedia;
  }

  public void setUrlMedia(String urlMedia) {
    this.urlMedia = urlMedia;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getVotes() {
    return votes;
  }

  public void setVotes(Integer votes) {
    this.votes = votes;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
