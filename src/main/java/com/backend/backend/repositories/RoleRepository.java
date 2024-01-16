package com.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.Role;

public interface RoleRepository extends CrudRepository<Role,Long> {
  Optional<Role> findByName(String name);

  @Modifying
  @Query("INSERT INTO Role (name) VALUES (?1)")
  void save(String name);

  // @Query("SELECT r FROM User u , Role r WHERE u.id = r.id AND u.id = ?1 AND ur.role_id = 2")
  // boolean getAdmin(Long userId);
}
