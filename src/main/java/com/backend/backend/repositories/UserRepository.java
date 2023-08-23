package com.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

  //JpaUserDetailsService , UserController (create)
  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> getUserByEmail(String email);

  //UserServiceImpl
  @Query("SELECT u FROM User u WHERE u.id != ?1 AND u.email = ?2")
  Optional<User> getUserCompareDifferentId(Long id, String email);

  //JwtAuthenticationFilter
  @Query("SELECT u.id FROM User u WHERE u.email = ?1")
  Long getUserId(String email);

  //UserController (remove)
  @Query("SELECT u FROM User u WHERE u.id = ?1")
  Optional<User> getUserById(Long id);
  
  @Modifying
  @Query("UPDATE User u SET u.isActive = 0 WHERE u.id = ?1")
  void eliminarUsuario(Long id);

  Page<User> findAll(Pageable pageable);
}
