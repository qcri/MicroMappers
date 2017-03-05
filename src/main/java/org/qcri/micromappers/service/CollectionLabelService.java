package org.qcri.micromappers.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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
			collectionLabel = refactorCollectionLabel(collectionLabel);
			if(collectionLabel != null && StringUtils.isNotBlank(collectionLabel.getTopic()) && 
											StringUtils.isNotBlank(collectionLabel.getFirstLabel()) &&
											StringUtils.isNotBlank(collectionLabel.getSecondLabel()) &&
											StringUtils.isNotBlank(collectionLabel.getFirstLabelTags()) &&
											StringUtils.isNotBlank(collectionLabel.getSecondLabelTags())
											){
				return collectionLabelRepository.save(collectionLabel);
			}else{
				return null;
			}
		}catch (Exception e) {
			logger.error("Error while adding collectionLabel to a collection", e);
			throw new MicromappersServiceException("Exception while adding collectionLabel to a collection", e);
		}
	} 

	private CollectionLabel refactorCollectionLabel(CollectionLabel collectionLabel){
		if(StringUtils.isNotBlank(collectionLabel.getTopic())){
			collectionLabel.setTopic(collectionLabel.getTopic().replaceAll("[^A-Za-z0-9]",""));
		}
		if(StringUtils.isNotBlank(collectionLabel.getFirstLabel())){
			collectionLabel.setFirstLabel(collectionLabel.getFirstLabel().replaceAll("[^A-Za-z0-9]",""));
		}
		if(StringUtils.isNotBlank(collectionLabel.getSecondLabel())){
			collectionLabel.setSecondLabel(collectionLabel.getSecondLabel().replaceAll("[^A-Za-z0-9]",""));
		}
		if(StringUtils.isNotBlank(collectionLabel.getFirstLabelTags())){
			//Removing all the characters except alphabets and digits and collect the unique keywords
			collectionLabel.setFirstLabelTags(Arrays.stream(collectionLabel.getFirstLabelTags().split(",")).map(tag -> tag.replaceAll("[^A-Za-z0-9]","")).distinct().collect(Collectors.joining(",")));
		}
		if(StringUtils.isNotBlank(collectionLabel.getSecondLabelTags())){
			//Removing all the characters except alphabets and digits and collect the unique keywords
			collectionLabel.setSecondLabelTags(Arrays.stream(collectionLabel.getSecondLabelTags().split(",")).map(tag -> tag.replaceAll("[^A-Za-z0-9]","")).distinct().collect(Collectors.joining(",")));
		}
		
		return collectionLabel;
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
	
	public void delete(CollectionLabel collectionLabel)
	{
		try{
				collectionLabelRepository.delete(collectionLabel);
		}catch (Exception e) {
			logger.error("Error while removing collectionLabel from a collection", e);
			throw new MicromappersServiceException("Exception while removing collectionLabel from a collection", e);
		}
	} 
	
}
