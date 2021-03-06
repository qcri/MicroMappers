package org.qcri.micromappers.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.AccountRepository;
import org.qcri.micromappers.utility.Constants;
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

	public Account getByUserName(String userName){
		try{
			return accountRepository.findByUserName(userName);
		}catch (Exception e) {
			logger.error("Error while getting account by userName : "+ userName, e);
			throw new MicromappersServiceException("Error while getting account by userName : "+ userName, e);
		}
	}
	
	public Account getById(Long id){
		try{
			return accountRepository.findById(id);
		}catch (Exception e) {
			logger.error("Error while getting account by id : "+ id, e);
			throw new MicromappersServiceException("Error while getting account by id : "+ id, e);
		}
	}

	public List<Account> getMailEnabled(){
		try{
			return accountRepository.findByMailEnabledTrue();
		}catch (Exception e) {
			logger.error("Error while getting accounts for mailEnabled", e);
			throw new MicromappersServiceException("Error while getting accounts for mailEnabled", e);
		}
	}

	public Account getSystemUser(){
		try{
			return accountRepository.findByUserName(Constants.SYSTEM_USER_NAME);
		}catch (Exception e) {
			logger.error("Error while getting System Account : ", e);
			throw new MicromappersServiceException("Error while getting System Account : ", e);
		}
	}
}
