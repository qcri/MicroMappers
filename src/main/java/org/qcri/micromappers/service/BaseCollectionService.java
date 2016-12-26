package org.qcri.micromappers.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
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
		collection.setAccount(user);
		collection.setStatus(CollectionStatus.RUNNING);
		try {
			collectionService.saveOrUpdate(collection);
			collaboratorService.addCollaborator(collection, user);
		} catch (MicromappersServiceException e) {
			logger.error(e.getMessage());
			throw new MicromappersServiceException(e.getMessage(), e);
		}
		return collection;
	}

	public ResponseWrapper prepareCollectionTask(Long id){
		try {
			Collection collection = collectionService.getById(id);
			if (!collection.getStatus().equals(CollectionStatus.TRASHED)) {
				//TODO Will update below code 
				/*Long userId = collection.getAccount().getId();
				Collection alreadyRunningCollection = collectionRepository.getRunningCollectionStatusByUser(userId);
				if (alreadyRunningCollection != null) {
					this.stop(alreadyRunningCollection.getId(), userId);
				}*/
				UserConnection userConnection = null;
				try{
					userConnection = userConnectionService.getByProviderIdAndUserId(collection.getProvider().getValue(), collection.getAccount().getUserName());
				}catch(Exception e){
					logger.error("Exception while fetching userConnection for userId: "+collection.getProvider().getValue() + "-" + collection.getAccount().getUserName());
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
		if(StringUtils.isBlank(collectionInfo.getCode())){
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.FAILED.toString(), "Collection code not present.");
		}
		
		Collection collection = collectionService.getByCode(collectionInfo.getCode());
		
		if(collection != null){
			collection.setProvider(CollectionType.valueOf(collectionInfo.getProvider()));
			collection.setDurationHours(collectionInfo.getDurationHours());
			collection.setFollow(collectionInfo.getFollow());
			collection.setGeo(collectionInfo.getGeo());
			collection.setGeoR(collectionInfo.getGeoR());
			collection.setLangFilters(collectionInfo.getLangFilters());
			collection.setFetchInterval(collectionInfo.getFetchInterval());
			collection.setFetchFrom(collectionInfo.getFetchFrom());
			if(StringUtils.isNotBlank(collectionInfo.getTrack())) {
				collection.setTrack(collectionInfo.getTrack().toLowerCase().trim());
			}
			
			try {
				Collection updatedCollection = collectionService.saveOrUpdate(collection);
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
