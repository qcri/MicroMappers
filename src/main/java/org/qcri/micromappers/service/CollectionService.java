package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionService
{
  private static Logger logger = Logger.getLogger(CollectionService.class);
  @Inject
  private CollectionRepository collectionRepository;
  
  public Collection create(Collection collection)
  {
	  try{
		  return collectionRepository.save(collection);
	  }catch (Exception e) {
		logger.error("Error while creating collection", e);
		throw new MicromappersServiceException("Exception while creating a collection", e);
	}
  }
  
  public Collection getById(Long id)
  {
	  try{
		  return collectionRepository.findById(id);
	  }catch (Exception e) {
		logger.error("Error while fetching collection by id", e);
		throw new MicromappersServiceException("Error while fetching collection by id", e);
	}
  }
  
}
