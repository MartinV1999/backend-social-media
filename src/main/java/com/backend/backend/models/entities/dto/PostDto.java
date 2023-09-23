package com.backend.backend.models.entities.dto;

import java.util.List;
import java.util.UUID;

public class PostDto {
  private Long id;
  private String title;
  private UserPostDto user;
  private List<PostPicturesDto> postPictures;
  private String description;
  private Integer votes;
  private List<CommentDto> comments;
  private UUID uuid;
  
  public PostDto(Long id, String title, UserPostDto user, List<PostPicturesDto> postPictures, String description, Integer votes, List<CommentDto> comments, UUID uuid) {
    this.id = id;
    this.title = title;
    this.user = user;
    this.postPictures = postPictures;
    this.description = description;
    this.votes = votes;
    this.comments = comments;
    this.uuid = uuid;
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

  public List<PostPicturesDto> getPostPictures() {
    return postPictures;
  }

  public void setPostPictures(List<PostPicturesDto> postPictures) {
    this.postPictures = postPictures;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

}
