package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collaborator;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
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
}
