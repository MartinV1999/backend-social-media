package com.backend.backend.services;

import java.util.List;
import java.util.Optional;

import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.models.entities.request.UserRequest;

public interface UserService {
  
  List<UserDto> findAll();

  UserDto save(User user);

  Optional<UserDto> update(UserRequest user, Long id);

  Optional<UserDto> findById(Long id);

  Optional<User> getUserById(Long id);
}
