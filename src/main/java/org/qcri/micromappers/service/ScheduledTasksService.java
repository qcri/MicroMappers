package org.qcri.micromappers.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.controller.BaseCollectionController;
import org.qcri.micromappers.entity.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasksService
{
	private static Logger logger = Logger.getLogger(ScheduledTasksService.class);

	@Autowired
	private BaseCollectionController baseCollectionController;

	@Autowired
	private CollectionService collectionService;

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
					baseCollectionController.getStatus(collection.getId());
				} catch (Exception e) {
					logger.error("Error while updating collection with Id: " + collection.getId(), e);
				}
			});
		}
	}
}
