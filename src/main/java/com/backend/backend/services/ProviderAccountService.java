package com.backend.backend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.ProvidersAccounts;

@Service
public interface ProviderAccountService {
  ProvidersAccounts save(ProvidersAccounts account);

  Optional<ProvidersAccounts> getAccountByEmail(String email);
}
