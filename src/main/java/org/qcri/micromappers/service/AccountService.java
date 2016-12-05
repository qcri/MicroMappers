package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService
{
  private static Logger logger = Logger.getLogger(AccountService.class);
  @Inject
  private AccountRepository accountRepository;
  
  public Account create(Account account)
  {
    return accountRepository.save(account);
  }
  
  public Account findByUserName(String userName){
	  return accountRepository.findByUserName(userName);
  }
}
