package org.qcri.micromappers.controller;

import java.io.IOException;

import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.service.BaseCollectionService;
import org.qcri.micromappers.service.CollectionLogService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.GenericCache;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.TwitterStreamTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kushal
 * RESTFul APIs to start and stop Twitter collections.
 */

@Primary
@RestController
@RequestMapping("/twitter")
public class TwitterCollectionController extends BaseCollectionController {

	@Autowired
	private BaseCollectionService baseCollectionService;
	@Autowired
	private CollectionService collectionService;
	@Autowired
	private CollectionLogService collectionLogService;

	private GenericCache cache = GenericCache.getInstance();

	public ResponseWrapper start(Long id) {

		logger.info("Starting the collection having collectionId: "+id);
		ResponseWrapper preparedCollectionTask = baseCollectionService.prepareCollectionTask(id, CollectionType.TWITTER);
		if(!preparedCollectionTask.getSuccess()){
			return preparedCollectionTask;
		}

		CollectionTask task = (CollectionTask) preparedCollectionTask.getData();

		logger.info("Collection start request received for " + task.getCollectionCode());

		//check if all twitter specific information is available in the request
		if (!task.checkSocialConfigInfo()) {
			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), 
					"One or more Twitter authentication token(s) are missing");
		}

		//check if query parameters are missing in the query
		if (!task.isToTrackAvailable()) {
			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), 
					"Missing tracking keywords. At least one keyword is required");
		}

		String collectionCode = task.getCollectionCode();

		//check if a task is already running with same configurations
		logger.info("Checking OAuth parameters for " + collectionCode);
		if (cache.isConfigExists(task)) {
			CollectionTask tempTask = cache.getTwitterConfig(collectionCode);
			if(tempTask != null){
				return new ResponseWrapper(tempTask, true, tempTask.getTwitterStatus().toString(), 
						tempTask.getTwitterStatus().toString());
			}
			
			String msg = "Provided OAuth configurations already in use. Please stop this collection and then start again.";
			logger.info(collectionCode + ": " + msg);

			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), msg);
		}

		logger.info("Initializing connection with Twitter streaming API for collection " + collectionCode);
		task.setTwitterStatus(CollectionStatus.RUNNING);

		try {
			TwitterStreamTracker tracker = new TwitterStreamTracker(task);
			tracker.start();

			String code = task.getCollectionCode();
			cache.incrTwtCounter(code, 0L);

			// if twitter streaming connection successful then change the status code
			task.setTwitterStatus(CollectionStatus.RUNNING);
			task.setStatusMessage(null);
			cache.setTwitterTracker(code, tracker);
			cache.setTwtConfigMap(code, task);

			//Adding a new log to CollectionLog
			collectionLogService.createByCollectionId(id, CollectionType.TWITTER);

			//Updating the status of collection in db
			collectionService.updateTwitterStatusById(id, CollectionStatus.RUNNING);
			return new ResponseWrapper(cache.getTwitterConfig(code), true, CollectionStatus.RUNNING.toString(), 
					CollectionStatus.RUNNING.toString());
		} catch (Exception ex) {
			logger.error("Exception in creating TwitterStreamTracker for collection " + collectionCode, ex);
			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), ex.getMessage());
		}
	}

	public ResponseWrapper stop(Long id) {
		logger.info("Stopping the collection having collectionId: "+id);
		ResponseWrapper stopTaskResponse = stopTask(id);

		if(stopTaskResponse == null) {
			stopTaskResponse = new ResponseWrapper(null, true, CollectionStatus.NOT_RUNNING.toString(),
					"Invalid key. No running collector found for the given id.");
		}else{
			//Update the endDate and collection count in collectionLog
			collectionLogService.stop(id, ((CollectionTask) stopTaskResponse.getData()).getTweetCount(), CollectionType.TWITTER);
		}
		collectionService.updateTwitterStatusById(id, CollectionStatus.NOT_RUNNING);
		((CollectionTask) stopTaskResponse.getData()).setTwitterStatus(CollectionStatus.NOT_RUNNING);
		return stopTaskResponse;
	}


	protected ResponseWrapper stopTask(Long id) {
		Collection collection = collectionService.getById(id);
		String collectionCode = collection.getCode();
		CollectionTask task = null;

		TwitterStreamTracker tracker = cache.getTwitterTracker(collectionCode);

		if (tracker != null) {
			try {
				tracker.close();
				task = cache.getTwitterConfig(collectionCode);
				clearCache(collectionCode);
			} catch (IOException e) {
				task = cache.getTwitterConfig(collectionCode);
				clearCache(collectionCode);
				return new ResponseWrapper(task, true, CollectionStatus.NOT_RUNNING.toString(), e.getMessage());
			}
			logger.info(collectionCode + ": " + "Collector has been successfully stopped.");
		} else {
			task = cache.getTwitterConfig(collectionCode);
			clearCache(collectionCode);
			logger.info("No collector instances found to be stopped with the given id:" + collectionCode);
		}

		if (task != null) {
			return new ResponseWrapper(task, true, CollectionStatus.NOT_RUNNING.toString(), null);
		}
		return null;
	}

	protected ResponseWrapper restart(Long id){
		logger.info("Stopping the collection having collectionId: "+id);
		stop(id);
		logger.info("Starting the collection having collectionId: "+id);
		return start(id);
	}

	public ResponseWrapper getStatus(Long id) {
		Collection collection = collectionService.getById(id);

		GenericCache cache = GenericCache.getInstance();
		CollectionTask twitterTask = cache.getTwitterConfig(collection.getCode());
		if (twitterTask != null) {
			collectionLogService.updateCount(id, twitterTask.getTweetCount(), CollectionType.TWITTER);

			if(collection.getTwitterStatus() != twitterTask.getTwitterStatus()){
				collectionService.updateTwitterStatusById(id, twitterTask.getTwitterStatus());
			}
			return new ResponseWrapper(twitterTask, true, ResponseCode.SUCCESS.toString(), null);
		}else if(collection.getTwitterStatus() != CollectionStatus.NOT_RUNNING){
			collection.setTwitterStatus(CollectionStatus.NOT_RUNNING);
			collectionService.updateTwitterStatusById(id, CollectionStatus.NOT_RUNNING);
		}
		return new ResponseWrapper(null, true, ResponseCode.SUCCESS.toString(), "Invalid key. No running collector found for the given id.");
	}


	private void clearCache(String collectionCode) {
		cache.delFailedCollection(collectionCode);
		cache.deleteTwtCounter(collectionCode);
		cache.delTwtConfigMap(collectionCode);
		cache.delTwitterTracker(collectionCode);
		cache.delReconnectAttempts(collectionCode);
	}

	
	@Override
	public ResponseWrapper getCount(Long id) {
		Long collectionCount = collectionLogService.getCountByCollectionIdAndProvider(id, CollectionType.TWITTER);
		return new ResponseWrapper(collectionCount, true, ResponseCode.SUCCESS.toString(), null);
	}
}
