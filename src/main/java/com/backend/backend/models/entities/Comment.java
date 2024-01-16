package com.backend.backend.models.entities;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  private String content;

  @ManyToOne
  private Post post;
  
  @NotNull
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date createdAt;

  @ManyToOne
  private User users;

  @PrePersist
  public void prePersist(){
    createdAt = new Date();
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

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  public User getUser() {
    return users;
  }

  public void setUser(User users) {
    this.users = users;
  }

}
