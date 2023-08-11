package com.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
  
  @Query("SELECT u FROM User u WHERE u.username = ?1")
  Optional<User> getUserByUsername(String username);

  //Optional<User> getUserByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.id = ?1")
  Optional<User> getUserById(Long id);

}
