package com.backend.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.Role;
import com.backend.backend.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Optional<Role> findByName(String name) {
    return roleRepository.findByName(name);
  }

  @Override
  public void save(String name) {
    roleRepository.save(name);
  }

  // @Override
  // public boolean getAdmin(Long userId) {
  //   try {
  //     Optional<User> op = userRepository.findById(userId);
  //     if(!op.isPresent()){
  //       throw new Exception ();
  //     }
  //     return roleRepository.getAdmin(userId);
  //   } catch (Exception e) {
  //     return false;
  //   }
  // }
  
}
