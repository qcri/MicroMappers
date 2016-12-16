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

	public Collection create(CollectionDetailsInfo collectionDetailsInfo)
	{
		Collection collection = null;

		Account user = util.getAuthenticatedUser();

		String track = collectionDetailsInfo.getTrack();

		if(!StringUtils.isEmpty(track)) {
			track = track.toLowerCase().trim();
			collectionDetailsInfo.setTrack(track);
		}
		if(StringUtils.isEmpty(track)) {
			return null;
		}

		collection = adaptCollectionDetailsInfoToCollection(collectionDetailsInfo, user);
		collection.setCount(0L);
		try {
			collectionService.create(collection);
			collaboratorService.addCollaborator(collection, user);
		} catch (MicromappersServiceException e) {
			logger.error("Error while creating a new collection", e);
			throw new MicromappersServiceException(e.getMessage(), e);
		}
		return collection;
	}

	public CollectionTask prepareCollectionTask(Long id){
		try {
			Collection collection = collectionService.getById(id);
			if (!collection.getStatus().equals(CollectionStatus.TRASHED)) {
	//TODO Will update below code 
				/*Long userId = collection.getAccount().getId();
				Collection alreadyRunningCollection = collectionRepository.getRunningCollectionStatusByUser(userId);
				if (alreadyRunningCollection != null) {
					this.stop(alreadyRunningCollection.getId(), userId);
				}*/
	
				return adaptCollectionToCollectionTask(collection);
			}
		} catch (MicromappersServiceException e) {
			logger.error("Error while fetching the collection by Id: "+id);
			throw new MicromappersServiceException("Error while fetching the collection by Id: "+id, e);
		}
		return null;
	}

	private CollectionTask adaptCollectionToCollectionTask(Collection collection) {
		CollectionTask task = new CollectionTask();
		UserConnection userconnection = null;
		try{
			userconnection = userConnectionService.getByProviderIdAndUserId(collection.getProvider().getValue(), collection.getAccount().getUserName());
		}catch(Exception e){
			logger.error("Exception while fetching userConnection for userId: "+collection.getProvider().getValue() + "-" + collection.getAccount().getUserName());
			throw new MicromappersServiceException("Exception while fetching userConnection for userId: "+collection.getProvider().getValue() + "-" + collection.getAccount().getUserName(), e);
		}
		
		task.setAccessToken(userconnection.getAccessToken());
		task.setAccessTokenSecret(userconnection.getSecret());
		task.setStatusCode(collection.getStatus());
		task.setCollectionName(collection.getName());
		task.setCollectionCode(collection.getCode());
		task.setToFollow(collection.getFollow());
		task.setToTrack(collection.getTrack());
		task.setGeoLocation(collection.getGeo());
		task.setGeoR(collection.getGeoR());
		task.setLanguageFilter(collection.getLangFilters());
		task.setFetchInterval(collection.getFetchInterval());
		task.setProvider(collection.getProvider());
		task.setFetchInterval(collection.getFetchInterval());
		task.setFetchFrom(collection.getFetchFrom());
		task.setLastExecutionTime(collection.getLastExecutionTime());
		return task;
	}


	private Collection adaptCollectionDetailsInfoToCollection(CollectionDetailsInfo collectionInfo, Account user) {
		Collection collection = new Collection();
		collection.setCode(collectionInfo.getCode());
		collection.setName(collectionInfo.getName());
		collection.setGlobalEventDefinition(collectionInfo.getGlobalEventDefinitionId());
		collection.setAccount(user);
		collection.setStatus(CollectionStatus.NOT_RUNNING);
		collection.setProvider(CollectionType.valueOf(collectionInfo.getProvider()));
		collection.setDurationHours(collectionInfo.getDurationHours());
		collection.setTrack(collectionInfo.getTrack());
		collection.setFollow(collectionInfo.getFollow());
		collection.setGeo(collectionInfo.getGeo());
		collection.setGeoR(collectionInfo.getGeoR());
		collection.setLangFilters(collectionInfo.getLangFilters());
		collection.setFetchInterval(collectionInfo.getFetchInterval());
		collection.setFetchFrom(collectionInfo.getFetchFrom());
		return collection;
	}

}
