package com.backend.backend.models.entities.dto;

import java.util.Date;
import java.util.List;

import com.backend.backend.models.entities.Role;

public class UserDto {
  private Long id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String rut;
  private String identificator;
  private Date birthday;
  private String address;
  private boolean admin;
  private Integer isActive;
  private List<Role> roles;
  
  public UserDto(){

  }
  
  public UserDto(Long id, String firstname, String lastname, String username, String email, String rut,
    String identificator, Date birthday, String address, boolean admin, Integer isActive, List<Role> roles) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.email = email;
    this.rut = rut;
    this.identificator = identificator;
    this.birthday = birthday;
    this.address = address;
    this.admin = admin;
    this.isActive = isActive;
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getRut() {
    return rut;
  }

  public void setRut(String rut) {
    this.rut = rut;
  }

  public String getIdentificator() {
    return identificator;
  }

  public void setIdentificator(String identificator) {
    this.identificator = identificator;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
  }

  

}
