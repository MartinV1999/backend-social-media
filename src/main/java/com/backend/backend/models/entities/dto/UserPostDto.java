package com.backend.backend.models.entities.dto;

public class UserPostDto {
  private Long id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String urlImage;
  
  public UserPostDto(){

  }
  
  public UserPostDto(Long id, String firstname, String lastname, String username, String email, String urlImage) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.email = email;
    this.urlImage = urlImage;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getUrlImage() {
    return urlImage;
  }

  public void setUrlImage(String urlImage) {
    this.urlImage = urlImage;
  }

  

}
