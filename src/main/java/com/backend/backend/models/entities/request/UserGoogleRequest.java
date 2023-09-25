package com.backend.backend.models.entities.request;

public class UserGoogleRequest {
  private String idOAuthToken;
  private String email;
  private String displayName;
  private String picture;
  
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPicture() {
    return picture;
  }
  public void setPicture(String picture) {
    this.picture = picture;
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getIdOAuthToken() {
    return idOAuthToken;
  }
  public void setIdOAuthToken(String idOAuthToken) {
    this.idOAuthToken = idOAuthToken;
  }

}
