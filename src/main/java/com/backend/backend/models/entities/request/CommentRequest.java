package com.backend.backend.models.entities.request;

import com.backend.backend.models.entities.Comment;

public class CommentRequest {
  private Long postId;
  private Long userId;
  private Comment comment;
  
  public Long getPostId() {
    return postId;
  }
  public void setPostId(Long postId) {
    this.postId = postId;
  }
  public Comment getComment() {
    return comment;
  }
  public void setComment(Comment comment) {
    this.comment = comment;
  }
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }

}
