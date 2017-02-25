package org.qcri.micromappers.service;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.CollectionLabel;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.repository.CollectionLabelRepository;
import org.springframework.stereotype.Service;

/**
 * @author Kushal
 *
 */
@Service
public class CollectionLabelService
{
	private static Logger logger = Logger.getLogger(CollectionLabelService.class);

	@Inject
	private CollectionLabelRepository collectionLabelRepository;
	public CollectionLabel create(CollectionLabel collectionLabel)
	{
		try{
			return collectionLabelRepository.save(collectionLabel);
		}catch (Exception e) {
			logger.error("Error while adding collectionLabel to a collection", e);
			throw new MicromappersServiceException("Exception while adding collectionLabel to a collection", e);
		}
	} 

	
	public CollectionLabel getByCollectionId(Long collectionId)
	{
		try{
			CollectionLabel collectionLabel = collectionLabelRepository.findByCollectionId(collectionId);
			return collectionLabel;
		}catch (Exception e) {
			logger.error("Exception while getting collectionLabel for a collectionId: "+collectionId, e);
			throw new MicromappersServiceException("Exception while getting collectionLabel for a collectionId: "+collectionId, e);
		}
	} 
	
}
