package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.CollectionLog;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollectionLogRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionLogService
{
  private static Logger logger = Logger.getLogger(CollectionLogService.class);
  
  @Inject
  private CollectionLogRepository collectionLogRepository;
  
  public CollectionLog create(CollectionLog collectionLog)
  {
	  try{
		  return collectionLogRepository.save(collectionLog);
	  }catch (Exception e) {
		logger.error("Error while adding log to a collectionLog", e);
		throw new MicromappersServiceException("Exception while adding  log to a collectionLog", e);
	}
  } 
  
}
