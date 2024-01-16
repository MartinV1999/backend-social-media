package com.backend.backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.backend.backend.models.entities.ProvidersAccounts;


public interface ProviderAccountRepository extends CrudRepository<ProvidersAccounts, Long>{

  // @Modifying
  // Optional<ProvidersAccounts> saveAccount(ProvidersAccounts providerAccount);
  @Query("SELECT p FROM ProvidersAccounts p WHERE p.email = ?1")
  Optional<ProvidersAccounts> getAccountByEmail(String email);
}
