package com.backend.backend.models.entities.dto;

public class PostPicturesDto {
  private Long id;
  private String url;
  private String filename;

  public PostPicturesDto(Long id, String url, String filename) {
    this.id = id;
    this.url = url;
    this.filename = filename;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

}
