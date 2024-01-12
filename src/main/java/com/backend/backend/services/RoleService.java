package com.backend.backend.services;

import java.util.Optional;

import com.backend.backend.models.entities.Role;

public interface RoleService {
  Optional<Role> findByName(String name);
  void save(String name);
  // boolean getAdmin(Long userId);
}
