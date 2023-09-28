package com.backend.backend.models.entities.request;

import java.sql.Date;
import java.util.UUID;

import com.backend.backend.models.entities.IUser;

public class UserRequest implements IUser {

  //private Long id;
  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String rut;
  private String identificator;
  private Date birthday;
  private String address;
  private String password;
  private boolean admin;
  private UUID uuid;

  //private List<Role> roles;

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

  @Override
  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }
  
}
