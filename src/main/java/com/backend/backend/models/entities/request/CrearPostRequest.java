package com.backend.backend.models.entities.request;

import com.backend.backend.models.entities.Post;

public class CrearPostRequest {
  private Long userId;
  private Post post;
  
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  public Post getPost() {
    return post;
  }
  public void setPost(Post post) {
    this.post = post;
  }

  

}
