package org.qcri.micromappers.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.CollectionLabel;
import org.qcri.micromappers.entity.UserConnection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.RoleType;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseCollectionService{

	private static Logger logger = Logger.getLogger(BaseCollectionService.class);

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private CollaboratorService collaboratorService;

	@Autowired
	private UserConnectionService userConnectionService;

	@Autowired
	private CollectionLabelService collectionLabelService;

	@Autowired 
	private Util util;

	public Boolean isCurrentUserPermitted(Long collectionId){
		Account user = util.getAuthenticatedUser();
		if(user.getRole() == RoleType.ADMIN){
			return Boolean.TRUE;
		}else{
			if(collaboratorService.isCollaboratorExists(collectionId, user.getId())){
				return Boolean.TRUE;
			}else{
				return Boolean.FALSE;
			}
		}
	}

	public Collection create(CollectionDetailsInfo collectionDetailsInfo)
	{
		Collection collection = null;
		Account user = util.getAuthenticatedUser();

		collection = collectionDetailsInfo.toCollection();
		if(collection.getGlobalEventDefinition() != null){
			Collection collectionWithAccountAndGlobalEventDefinition = collectionService.getByAccountAndGlobalEventDefinition(user, collection.getGlobalEventDefinition());
			if(collectionWithAccountAndGlobalEventDefinition != null){
				throw new MicromappersServiceException("A collection already existed for this user with this event.");
			}
		}else if(collection.getGlideMaster() != null){
			Collection collectionWithAccountAndGlideMaster = collectionService.getByAccountAndGlideMaster(user, collection.getGlideMaster());
			if(collectionWithAccountAndGlideMaster != null){
				throw new MicromappersServiceException("A collection already existed for this user with this event.");
			}
		}
		collection.setAccount(user);
		collection.setTwitterStatus(CollectionStatus.NOT_RUNNING);
		collection.setFacebookStatus(CollectionStatus.NOT_RUNNING);
		try {
			if(StringUtils.isBlank(collection.getName()) || StringUtils.isBlank(collection.getCode())){
				return null;
			}
			collectionService.saveOrUpdate(collection);
			collaboratorService.addCollaborator(collection, user);
		} catch (MicromappersServiceException e) {
			logger.error(e.getMessage());
			throw new MicromappersServiceException(e.getMessage(), e);
		}

		try{
			CollectionLabel collectionLabel = collectionDetailsInfo.getCollectionLabel();
			collectionLabel.setCollection(collection);
			collectionLabelService.create(collectionLabel);
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return collection;
	}

	public ResponseWrapper prepareCollectionTask(Long id, CollectionType provider){
		try {
			Collection collection = collectionService.getById(id);
			if (!collection.getTwitterStatus().equals(CollectionStatus.TRASHED) || !collection.getFacebookStatus().equals(CollectionStatus.TRASHED)) {
				UserConnection userConnection = null;
				try{
					userConnection = userConnectionService.getByProviderIdAndUserId(provider.getValue(), collection.getAccount().getUserName());
				}catch(Exception e){
					logger.error("Exception while fetching userConnection for userId: "+provider.getValue() + "-" + collection.getAccount().getUserName());
					return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Error while getting the user.");
				}

				CollectionTask collectionTask = collection.toCollectionTask(userConnection);
				return new ResponseWrapper(collectionTask, true, ResponseCode.SUCCESS.toString(), null);
			}else{
				return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "This collection was trashed.");
			}
		} catch (MicromappersServiceException e) {
			logger.error("Error while fetching the collection by Id: "+id, e);
			return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Error while getting the collection.");
		}
	}


	public ResponseWrapper update(CollectionDetailsInfo collectionInfo) {
		if(collectionInfo.getId() == null){
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.FAILED.toString(), "Collection id not present.");
		}

		Collection collection = collectionService.getById(collectionInfo.getId());

		if(collection != null){
			collection.setProvider(CollectionType.valueOf(collectionInfo.getProvider()));
			collection.setDurationHours(collectionInfo.getDurationHours());
			collection.setLangFilters(collectionInfo.getLangFilters());
			collection.setFetchInterval(collectionInfo.getFetchInterval());
			collection.setFetchFrom(collectionInfo.getFetchFrom());
			if(StringUtils.isNotBlank(collectionInfo.getTrack())) {
				collection.setTrack(collectionInfo.getTrack().toLowerCase().trim());
			}
			if(StringUtils.isNotBlank(collectionInfo.getSubscribedProfiles())) {
				collection.setSubscribedProfiles(collectionInfo.getSubscribedProfiles());
			}

			try {
				Collection updatedCollection = collectionService.saveOrUpdate(collection);

				try{
					CollectionLabel oldCollectionLabel = collectionLabelService.getByCollectionId(collectionInfo.getId());
					if(oldCollectionLabel != null){
						if(collectionInfo.getCollectionLabel() != null){
							oldCollectionLabel.setTopic(collectionInfo.getCollectionLabel().getTopic());
							oldCollectionLabel.setFirstLabel(collectionInfo.getCollectionLabel().getFirstLabel());
							oldCollectionLabel.setSecondLabel(collectionInfo.getCollectionLabel().getSecondLabel());
							oldCollectionLabel.setFirstLabelTags(collectionInfo.getCollectionLabel().getFirstLabelTags());
							oldCollectionLabel.setSecondLabelTags(collectionInfo.getCollectionLabel().getSecondLabelTags());
							collectionLabelService.create(oldCollectionLabel);
						}else{
							collectionLabelService.delete(oldCollectionLabel);
						}
					}else{
						if(collectionInfo.getCollectionLabel() != null){
							collectionLabelService.create(collectionInfo.getCollectionLabel());
						}
					}
				}catch (Exception e) {
					logger.error("Exception while updating collection label", e);
				}

				return new ResponseWrapper(updatedCollection.toCollectionDetailsInfo(), Boolean.TRUE, ResponseCode.SUCCESS.toString(), null);
			} catch (MicromappersServiceException e) {
				logger.error("Error while updating collection", e);
				return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.FAILED.toString(), "Error while updating collection.");
			}
		}else{
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.FAILED.toString(), "Collection not found with the given collection code : " + collectionInfo.getCode());
		}
	}

}
