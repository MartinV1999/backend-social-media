package com.backend.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.models.entities.IUser;
import com.backend.backend.models.entities.Role;
import com.backend.backend.models.entities.User;
import com.backend.backend.models.entities.dto.UserDto;
import com.backend.backend.models.entities.dto.mapper.DtoMapperUser;
import com.backend.backend.models.entities.request.UserRequest;
import com.backend.backend.repositories.RoleRepository;
import com.backend.backend.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = (List<User>) userRepository.findAll();

    return users
      .stream()
      .map(u -> DtoMapperUser.builder().setUser(u).build())
      .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserDto save(User user) {
    if(user.getPassword() != null){
      user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
    user.setRoles(getRoles(user));
    return DtoMapperUser.builder().setUser(userRepository.save(user)).build();
  }
  
  private List<Role> getRoles(IUser user){
    Optional<Role> ou = roleRepository.findByName("ROLE_USER");
    List<Role> roles = new ArrayList<>();
    if(ou.isPresent()){
      roles.add(ou.orElseThrow());
    }

    if(user.isAdmin()){
      Optional<Role> oa = roleRepository.findByName("ROLE_ADMIN");
      if(oa.isPresent()){
        roles.add(oa.orElseThrow());
      }
    }
    return roles;
  }

  @Override
  public Optional<UserDto> findById(Long id) {
    return userRepository.findById(id)
      .map(u -> DtoMapperUser
      .builder()
      .setUser(u)
      .build());
  }

  @Override
  public Optional<UserDto> update(UserRequest user, Long id) {
    Optional<User> o = userRepository.findById(id);
    Optional<User> os = userRepository.getUserCompareDifferentId(id, user.getEmail());
    User userOptional = null;
    if(o.isPresent() && !os.isPresent()){
      User userDb = o.orElseThrow();
      userDb.setRoles(getRoles(user));
      userDb.setFirstname(user.getFirstname());
      userDb.setLastname(user.getLastname());
      userDb.setUsername(user.getUsername());
      userDb.setEmail(user.getEmail());
      userDb.setRut(user.getRut());
      userDb.setIdentificator(user.getIdentificator());
      userDb.setBirthday(user.getBirthday());
      userDb.setAddress(user.getAddress());
      userOptional = userRepository.save(userDb);
    }
    return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
  }

  @Override
  public Optional<User> getUserById(Long id) {
    return userRepository.getUserById(id);
  }

  @Override
  @Transactional
  public void removeUser(Long id) {
    //Eliminado logico
    userRepository.eliminarUsuario(id);
  }

  @Override
  public Long getUserId(String email) {
    return userRepository.getUserId(email);
  }

  @Override
  public Optional<User> getUserByEmail(String email) {
    return userRepository.getUserByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> findAll(Pageable pageable) {
    Page<User> usersPage = userRepository.findAll(pageable);
    return usersPage.map(u -> DtoMapperUser.builder().setUser(u).build());
  }

  @Override
  public void setUserNative(String username, String email, String urlImage, Integer isActive, String password) {
    userRepository.setUserNative(username, email, passwordEncoder.encode(password),  urlImage, isActive);
    Optional<UserDto> u = getUserByEmailDto(email);
    if(u.isPresent()){
      Optional<Role> ou = roleRepository.findByName("ROLE_USER");
      List<Role> roles = new ArrayList<>();
      if(ou.isPresent()){
        roles.add(ou.orElseThrow());
      }
      userRepository.setRoleNative(u.orElseThrow().getId(), ou.orElseThrow().getId());
    }

  }

  @Override
  public Optional<UserDto> getUserByEmailDto(String email) {
    return userRepository.getUserByEmailDto(email)
      .map(u -> DtoMapperUser.builder().setUser(u).build());
  }
}