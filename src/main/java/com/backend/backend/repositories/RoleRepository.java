package com.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.Role;

public interface RoleRepository extends CrudRepository<Role,Long> {
  Optional<Role> findByName(String name);
}
