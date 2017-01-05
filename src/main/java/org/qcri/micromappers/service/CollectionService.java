package org.qcri.micromappers.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollectionRepository;
import org.qcri.micromappers.utility.CollectionStatus;
import org.springframework.stereotype.Service;

@Service
public class CollectionService
{
	private static Logger logger = Logger.getLogger(CollectionService.class);
	@Inject
	private CollectionRepository collectionRepository;

	public Collection saveOrUpdate(Collection collection)
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

	public Collection getByCode(String code)
	{
		try{
			return collectionRepository.findByCode(code);
		}catch (Exception e) {
			logger.error("Error while fetching collection by code", e);
			throw new MicromappersServiceException("Error while fetching collection by code", e);
		}
	}

	public Boolean updateStatusByCode(String code, CollectionStatus collectionStatus){
		try{
			int updateStatus = collectionRepository.updateStatusByCode(code, collectionStatus);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while updating collection status by code", e);
			throw new MicromappersServiceException("Error while updating collection status by code", e);
		}
	}

	public Boolean updateStatusById(Long id, CollectionStatus collectionStatus){
		try{
			int updateStatus = collectionRepository.updateStatusById(id, collectionStatus);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while updating collection status by id", e);
			throw new MicromappersServiceException("Error while updating collection status by id", e);
		}
	}

	public List<Collection> getAllRunningCollections(){
		try{
			List<CollectionStatus> statuses = new ArrayList<CollectionStatus>(3);
			statuses.add(CollectionStatus.RUNNING);
			statuses.add(CollectionStatus.RUNNING_WARNING);
			statuses.add(CollectionStatus.WARNING);
			return collectionRepository.findByStatusIn(statuses);
		}catch (Exception e) {
			logger.error("Error while getting running collections from db", e);
			throw new MicromappersServiceException("Error while getting running collections from db", e);
		}
	}
	
	public Boolean isCollectionNameExists(String name){
		try{
			Long count = collectionRepository.countByNameIgnoreCase(name);
			if(count !=null && count > 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}catch (Exception e) {
			logger.error("Error while checking isCollectionNameExists", e);
			throw new MicromappersServiceException("Error while checking isCollectionNameExists", e);
		}
	} 
}
