package org.qcri.micromappers.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collaborator;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.AccountDTO;
import org.qcri.micromappers.repository.CollaboratorRepository;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService
{
  private static Logger logger = Logger.getLogger(CollaboratorService.class);
  @Inject
  private CollaboratorRepository collaboratorRepository;
  
  public Collaborator create(Collaborator collaborator)
  {
	  try{
		  return collaboratorRepository.save(collaborator);
	  }catch (Exception e) {
		logger.error("Error while adding collaborator to a collection", e);
		throw new MicromappersServiceException("Exception while adding collaborator to a collection", e);
	}
  } 
  
  public Collaborator addCollaborator(Collection collection, Account account)
  {
	  try{
		  Collaborator collaborator = new Collaborator();
		  collaborator.setAccount(account);
		  collaborator.setCollection(collection);
		  return collaboratorRepository.save(collaborator);
	  }catch (Exception e) {
		logger.error("Error while adding collaborator to a collection", e);
		throw new MicromappersServiceException("Exception while adding collaborator to a collection", e);
	}
  } 
  
  public Boolean isCollaboratorExists(Long collectionId, Long accountId)
  {
	  try{
		  Long count = collaboratorRepository.countByCollectionIdAndAccountId(collectionId, accountId);
		  if(count !=null && count > 0){
			  return Boolean.TRUE;
		  }else{
			  return Boolean.FALSE;
		  }
	  }catch (Exception e) {
		logger.error("Error while checking isCollaboratorExists with collection Id and account Id", e);
		throw new MicromappersServiceException("Error while checking isCollaboratorExists with collection Id and account Id", e);
	}
  } 
  
  public List<AccountDTO> getCollaboratorsByCollection(Long collectionId)
  {
	  try{
		  List<Collaborator> collaborators = collaboratorRepository.findByCollectionId(collectionId);
		  List<AccountDTO> collaboratorsDTO = collaborators.stream().map(c -> c.getAccount().toDTO()).collect(Collectors.toList());
		  return collaboratorsDTO;
	  }catch (Exception e) {
		logger.error("Exception while getting collaborators for a collectionId: "+collectionId, e);
		throw new MicromappersServiceException("Exception while getting collaborators for a collectionId: "+collectionId, e);
	}
  } 
}
