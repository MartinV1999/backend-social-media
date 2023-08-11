package com.backend.backend.models.entities.dto;

public class CommentPostDto {
  private Long id;
  private UserPostDto user;
  private String urlMedia;
  private String description;
  private Integer votes;

  public CommentPostDto(Long id, UserPostDto user, String urlMedia, String description, Integer votes) {
    this.id = id;
    this.user = user;
    this.urlMedia = urlMedia;
    this.description = description;
    this.votes = votes;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
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
  
  public UserPostDto getUser() {
    return user;
  }
  public void setUser(UserPostDto user) {
    this.user = user;
  }

  
}
