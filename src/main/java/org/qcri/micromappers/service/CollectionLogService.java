package org.qcri.micromappers.service;

import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.CollectionLog;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollectionLogRepository;
import org.qcri.micromappers.utility.Util;
import org.springframework.stereotype.Service;

@Service
public class CollectionLogService
{
	private static Logger logger = Logger.getLogger(CollectionLogService.class);

	@Inject
	private CollectionService collectionService;

	@Inject
	private CollectionLogRepository collectionLogRepository;

	@Inject
	private Util util;

	@Inject
	private AccountService accountService;

	public CollectionLog saveOrUpdate(CollectionLog collectionLog)
	{
		try{
			return collectionLogRepository.save(collectionLog);
		}catch (Exception e) {
			logger.error("Error while adding log to a collectionLog", e);
			throw new MicromappersServiceException("Exception while adding  log to a collectionLog", e);
		}
	} 

	public CollectionLog getLatestByCollectionId(Long collectionId)
	{
		try{
			return collectionLogRepository.findFirstByCollectionIdOrderByStartDateDesc(collectionId);
		}catch (Exception e) {
			logger.error("Error while finding latest log for collectionId : "+ collectionId, e);
			throw new MicromappersServiceException("Exception while finding latest log for collectionId : "+ collectionId, e);
		}
	} 

	public Boolean createByCollectionCode(String collectionCode)
	{
		try{
			Collection collection = collectionService.getByCode(collectionCode);
			CollectionLog collectionLog = new CollectionLog(collection);
			collectionLog.setStartDate(new Date());
			collectionLog.setCount(0L);
			Account authenticatedAccount = getAuthenticatedAccount();
			collectionLog.setUpdatedBy(authenticatedAccount);
			saveOrUpdate(collectionLog);
			return Boolean.TRUE;
		}catch (Exception e) {
			logger.error("Error while creating a new log for collectionCode: "+ collectionCode, e);
			return Boolean.FALSE;
		}
	} 

	/**
	 * Updating the endDate and collection Count in collectionLog
	 * @param collectionId
	 * @param count
	 * @return
	 */
	public Boolean stop(Long collectionId, Long count)
	{
		try{
			CollectionLog latestByCollectionLog = getLatestByCollectionId(collectionId);
			if(latestByCollectionLog != null && latestByCollectionLog.getEndDate() == null){
				latestByCollectionLog.setEndDate(new Date());
				latestByCollectionLog.setCount(count);
				Account authenticatedAccount = getAuthenticatedAccount();
				latestByCollectionLog.setUpdatedBy(authenticatedAccount);
				saveOrUpdate(latestByCollectionLog);
			}
			return Boolean.TRUE;
		}catch (Exception e) {
			logger.error("Error while stopping collectionLog for collectionId: "+ collectionId, e);
			return Boolean.FALSE;
		}
	}

	private Account getAuthenticatedAccount() {
		try {
			Account authenticatedUser = util.getAuthenticatedUser();
			if(authenticatedUser != null){
				return authenticatedUser;
			}else{
				Account systemUser = accountService.getSystemUser();
				return systemUser;
			}
		} catch (Exception e) {
			Account systemUser = accountService.getSystemUser();
			return systemUser;
		}
	} 

	public Boolean updateCount(Long collectionId, Long count)
	{
		try{
			CollectionLog latestByCollectionLog = getLatestByCollectionId(collectionId);
			if(latestByCollectionLog != null){
				latestByCollectionLog.setCount(count);
				saveOrUpdate(latestByCollectionLog);
			}
			return Boolean.TRUE;
		}catch (Exception e) {
			logger.error("Error while updating count in collectionLog for collectionId: "+ collectionId, e);
			return Boolean.FALSE;
		}
	}
}
