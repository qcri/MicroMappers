package org.qcri.micromappers.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.controller.CollectionController;
import org.qcri.micromappers.controller.FacebookCollectionController;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.utility.GenericCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasksService
{
	@Autowired
	private CollectionController collectionController;
	@Autowired
	private CollectionService collectionService;
	@Autowired
	private FacebookCollectionController facebookCollectionController;

	private static Logger logger = Logger.getLogger(ScheduledTasksService.class);


	@Scheduled(fixedDelay = 10 * 60 * 1000) // 10 minutes - in milliseconds
	private void scheduledTaskUpdateCollections() {
		List<Collection> collections;
		try {
			collections = collectionService.getAllRunningCollections();
		} catch (Exception e) {
			logger.error("Error while executing update collections scheduled task", e);
			return;
		}
		if(CollectionUtils.isNotEmpty(collections)){
			collections.parallelStream().forEach(collection -> {
				try {
					logger.info("Scheduled 10 min update for collectionId: "+ collection.getId());
					collectionController.getCollectionStatus(collection.getId());
				} catch (Exception e) {
					logger.error("Error while updating collection with Id: " + collection.getId(), e);
				}
			});
		}
	}
	
	
	@Scheduled(cron = "0 0 0/1 * * ?") //Every hour
	public void scheduledTaskUpdateFacebookCollections() {
		List<CollectionTask> collectionsToRun = GenericCache.getInstance().getEligibleFacebookCollectionsToRun();
		collectionsToRun.forEach(task -> facebookCollectionController.rerunCollection(task.getCollectionCode()));
	}
}
