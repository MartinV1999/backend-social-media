package com.backend.backend.models.entities.dto;

import java.util.UUID;

public class PostPicturesDto {
  private Long id;
  private String url;
  private String filename;
  private UUID uuid;

  public PostPicturesDto(Long id, String url, String filename, UUID uuid) {
    this.id = id;
    this.url = url;
    this.filename = filename;
    this.uuid = uuid;
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

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  

}
