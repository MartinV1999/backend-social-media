package com.backend.backend.models.entities.dto.mapper;

import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;

public class DtoMapperUser {
  private User user;
  
  private DtoMapperUser(){

  }

  public static DtoMapperUser builder(){
    return new DtoMapperUser();
  }

  public DtoMapperUser setUser(User user){
    this.user = user;
    return this;
  }

  public UserDto build(){
    if(user == null){
      throw new RuntimeException("Debe pasar el entity user!");
    }
    boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
    return new UserDto(
      this.user.getId(),
      this.user.getFirstname(),
      this.user.getLastname(),
      this.user.getUsername(),
      this.user.getEmail(),
      this.user.getRut(),
      this.user.getIdentificator(),
      this.user.getBirthday(),
      this.user.getAddress(),
      isAdmin,
      this.user.getIsActive(),
      this.user.getRoles(),
      this.user.getUrlImage(),
      this.user.getIsComplete(),
      this.user.getUuid());
  }
}
