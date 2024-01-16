package com.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO users (username,email,password,url_image, is_Active) VALUES (:username,:email, :password,:urlImage, :isActive)", nativeQuery = true)
  void setUserNative(@Param("username") String username, @Param("email") String email, @Param("password") String password, @Param("urlImage") String urlImage, @Param("isActive") Integer isActive);

  @Query("SELECT u FROM User u WHERE u.email = ?1")
  Optional<User> getUserByEmailDto(String email);

  @Modifying
  @Transactional
  @Query(value = "INSERT INTO users_roles (user_id, role_id) VALUES (:userId, :roleId)" , nativeQuery = true)
  void setRoleNative(@Param("userId") Long userId, @Param("roleId") Long roleId );

}
