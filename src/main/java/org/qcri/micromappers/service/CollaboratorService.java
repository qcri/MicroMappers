package org.qcri.micromappers.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collaborator;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollaboratorRepository;
import org.qcri.micromappers.utility.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService
{
	private static Logger logger = Logger.getLogger(CollaboratorService.class);

	@Inject
	private CollaboratorRepository collaboratorRepository;

	@Inject
	private CollectionService collectionService;

	@Inject
	private AccountService accountService;

	public Collaborator create(Collaborator collaborator)
	{
		try{
			return collaboratorRepository.save(collaborator);
		}catch (Exception e) {
			logger.error("Error while adding collaborator to a collection", e);
			throw new MicromappersServiceException("Exception while adding collaborator to a collection", e);
		}
	} 

	public Boolean addCollaborator(Collection collection, Account account)
	{
		if(account != null && collection != null){
			try{
				Collaborator collaborator = null;
				Boolean collaboratorExists = isCollaboratorExists(collection.getId(), account.getId());
				if(collaboratorExists == null || !collaboratorExists ){
					collaborator = new Collaborator();
					collaborator.setAccount(account);
					collaborator.setCollection(collection);
					collaborator = collaboratorRepository.save(collaborator);
					if(collaborator != null){
						return Boolean.TRUE;
					}
				}
				else{
					return Boolean.TRUE;
				}

			}catch (Exception e) {
				logger.error("Error while adding collaborator to a collection", e);
				throw new MicromappersServiceException("Exception while adding collaborator to a collection", e);
			}
		}
		return Boolean.FALSE;
	}

	public Boolean addCollaborator(Long collectionId, Long accountId)
	{
		try {
			Collection collection = collectionService.getById(collectionId);
			if(collection != null){
				Account account = accountService.getById(accountId);
				if(account != null){
					Boolean collaboratorAdded = addCollaborator(collection, account);
					if(collaboratorAdded != null && collaboratorAdded == Boolean.TRUE ){
						return Boolean.TRUE;
					}
				}
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			logger.error("Error while adding collaborator to the collection", e);
			throw new MicromappersServiceException("Error while adding collaborator to the collection", e);
		}
	}


	public Boolean removeCollaborator(Long collectionId, Long accountId)
	{
		try {
			if(collectionId != null && accountId != null){
				collaboratorRepository.deleteByCollectionIdAndAccountId(collectionId, accountId);
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			logger.error("Error while removing collaborator from the collection", e);
			return Boolean.FALSE;
		}
	}


	public Boolean isCollaboratorExists(Long collectionId, Long accountId)
	{
		try{
			Long count = collaboratorRepository.countByCollectionIdAndAccountId(collectionId, accountId);
			if(count !=null && count > 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}catch (Exception e) {
			logger.error("Error while checking isCollaboratorExists with collection Id and account Id", e);
			throw new MicromappersServiceException("Error while checking isCollaboratorExists with collection Id and account Id", e);
		}
	} 

	public List<Account> getCollaboratorsByCollection(Long collectionId)
	{
		try{
			List<Collaborator> collaborators = collaboratorRepository.findByCollectionId(collectionId);
			List<Account> collaboratorsDTO = collaborators.stream().map(c -> c.getAccount()).collect(Collectors.toList());
			return collaboratorsDTO;
		}catch (Exception e) {
			logger.error("Exception while getting collaborators for a collectionId: "+collectionId, e);
			throw new MicromappersServiceException("Exception while getting collaborators for a collectionId: "+collectionId, e);
		}
	} 

	public Collaborator getByCollectionAndAccount(Long collectionId, Long accountId)
	{
		try{
			Collaborator collaborator = collaboratorRepository.findByCollectionIdAndAccountId(collectionId, accountId);
			return collaborator;
		}catch (Exception e) {
			logger.error("Exception while getting collaborator for a collectionId: "+collectionId + " and accountId: "+accountId, e);
			throw new MicromappersServiceException("Exception while getting collaborator for a collectionId: "+collectionId + " and accountId: "+accountId, e);
		}
	} 
	
	public Page<Collaborator> getAllByPageAndAccount(Account account, PageRequest pageRequest) {
		
        return collaboratorRepository.findByAccount(account, pageRequest);
	}

	public List<Collaborator> getAllByAccount(Account account) {
		if(account.getRole().equals(RoleType.ADMIN)){
			return collaboratorRepository.findAll();
		}
		return collaboratorRepository.findByAccount(account);
	}
	
}
