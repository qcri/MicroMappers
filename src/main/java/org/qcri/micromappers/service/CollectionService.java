package org.qcri.micromappers.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collaborator;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollectionRepository;
import org.qcri.micromappers.utility.CollectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class CollectionService
{	
	@Inject
	private CollectionRepository collectionRepository;
	@Inject
	private CollaboratorService collaboratorService;

	private static Logger logger = Logger.getLogger(CollectionService.class);


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


	public Boolean updateTwitterStatusById(Long id, CollectionStatus twitterStatus){
		try{
			int updateStatus = collectionRepository.updateTwitterStatusById(id, twitterStatus);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while updating twitter status by id", e);
			return Boolean.FALSE;
		}
	}

	public Boolean updateFacebookStatusById(Long id, CollectionStatus facebookStatus){
		try{
			int updateStatus = collectionRepository.updateFacebookStatusById(id, facebookStatus);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while updating facebook status by id", e);
			return Boolean.FALSE;
		}
	}
	
	public Boolean updateFacebookStatusAndLastExecutionTimeById(Long id, CollectionStatus facebookStatus, Date lastExecutiontime){
		try{
			int updateStatus = collectionRepository.updateFacebookStatusAndLastExecutionTimeById(id, facebookStatus, lastExecutiontime);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while updating facebook status and last execution time by id", e);
			return Boolean.FALSE;
		}
	}
	
	public Boolean updateFacebookLastExecutionTimeById(Long id, Date lastExecutiontime){
		try{
			int updateStatus = collectionRepository.updateFacebookLastExecutionTimeById(id, lastExecutiontime);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while updating facebook last execution time by id", e);
			return Boolean.FALSE;
		}
	}

	public List<Collection> getAllRunningCollections(){
		try{
			List<CollectionStatus> statuses = new ArrayList<CollectionStatus>(3);
			statuses.add(CollectionStatus.RUNNING);
			statuses.add(CollectionStatus.RUNNING_WARNING);
			statuses.add(CollectionStatus.WARNING);
			return collectionRepository.findByTwitterStatusOrFacebookStatusIn(statuses);
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


	/** Return all the collections in which the user is collaborator
	 * @param account
	 * @param pageNumber
	 * @param sortDirection 
	 * @param sortColumn 
	 * @param pageSize 
	 * @return all the collections in which the user is collaborator
	 */
	public Page<Collection> getAllByPage(Account account, Integer pageNumber, Integer pageSize, String sortColumn, Direction sortDirection) {

		PageRequest request =
				new PageRequest(pageNumber - 1, pageSize, sortDirection, sortColumn);
		Page<Collaborator> pagedCollaborators = collaboratorService.getAllByPageAndAccount(account, request);

		Page<Collection> pagedCollections = pagedCollaborators.map(pc -> pc.getCollection());
		return pagedCollections;
	}

	public Boolean trashCollectionById(Long id) {
		try{
			int updateStatus = collectionRepository.updatingTwitterStatusAndFacebookStatusAndTrashStatusById(id, CollectionStatus.TRASHED, CollectionStatus.TRASHED, Boolean.TRUE);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while deleting collection by id", e);
			return Boolean.FALSE;
		}
	}

	public Boolean untrashCollectionById(Long id) {
		try{
			int updateStatus = collectionRepository.updatingTwitterStatusAndFacebookStatusAndTrashStatusById(id, CollectionStatus.NOT_RUNNING, CollectionStatus.NOT_RUNNING, Boolean.FALSE);
			if(updateStatus == 1){
				return Boolean.TRUE; 
			}else{
				return Boolean.FALSE;
			}
		}catch (Exception e) {
			logger.error("Error while restoring collection by id", e);
			return Boolean.FALSE;
		}
	}

	public Collection getByAccountAndGlobalEventDefinition(Account account, GlobalEventDefinition globalEventDefinition)
	{
		try{
			return collectionRepository.getByAccountAndGlobalEventDefinition(account, globalEventDefinition);
		}catch (Exception e) {
			logger.error("Error while fetching collection by Account and GlobalEventDefinition", e);
			throw new MicromappersServiceException("Error while fetching collection by Account and GlobalEventDefinition", e);
		}
	}

	public Collection getByAccountAndGlideMaster(Account account, GlideMaster glideMaster)
	{
		try{
			return collectionRepository.getByAccountAndGlideMaster(account, glideMaster);
		}catch (Exception e) {
			logger.error("Error while fetching collection by Account and GlideMaster", e);
			throw new MicromappersServiceException("Error while fetching collection by Account and GlideMaster", e);
		}
	}
}
