package com.backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.models.entities.request.UserRequest;

public interface UserService {
  
  List<UserDto> findAll();

  Page<UserDto> findAll(Pageable pageable);

  UserDto save(User user);

  Optional<UserDto> update(UserRequest user, Long id);

  Optional<UserDto> findById(Long id);

  Optional<UserDto> getUserByEmailDto(String email);

  Optional<User> getUserById(Long id);

  void removeUser(Long id);

  Long getUserId(String email);

  Optional<User> getUserByEmail(String email);

  void setUserNative(String username, String email, String urlImage, Integer isActive, String password);



}
