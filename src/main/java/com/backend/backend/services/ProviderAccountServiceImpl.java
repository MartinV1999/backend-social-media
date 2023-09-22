package com.backend.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.models.entities.ProvidersAccounts;
import com.backend.backend.repositories.ProviderAccountRepository;

@Service
public class ProviderAccountServiceImpl implements ProviderAccountService {

  @Autowired
  private ProviderAccountRepository providerAccountRepository;

  @Override
  public ProvidersAccounts save(ProvidersAccounts account) {
    return providerAccountRepository.save(account);
  }

  @Override
  public Optional<ProvidersAccounts> getAccountByEmail(String email) {
    return providerAccountRepository.getAccountByEmail(email);
  }
  
}
