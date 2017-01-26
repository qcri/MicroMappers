package org.qcri.micromappers.controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.UserConnection;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.models.FacebookProfile;
import org.qcri.micromappers.service.BaseCollectionService;
import org.qcri.micromappers.service.CollectionLogService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.service.UserConnectionService;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.FacebookFeedTracker;
import org.qcri.micromappers.utility.GenericCache;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facebook")
public class FacebookCollectionController extends BaseCollectionController{

	@Autowired
	private BaseCollectionService baseCollectionService;
	@Autowired
	private CollectionService collectionService;	
	@Autowired
	private CollectionLogService collectionLogService;	
	@Autowired
	private UserConnectionService userConnectionService;
	@Autowired
	private Util util;

	private static Logger logger = Logger.getLogger(FacebookCollectionController.class);
	private GenericCache cache = GenericCache.getInstance();

	
	@Override
	public ResponseWrapper start(Long id) {
		logger.info("Starting the collection having collectionId: "+id);
		ResponseWrapper preparedCollectionTask = baseCollectionService.prepareCollectionTask(id, CollectionType.FACEBOOK);
		if(!preparedCollectionTask.getSuccess()){
			return preparedCollectionTask;
		}

		CollectionTask task = (CollectionTask) preparedCollectionTask.getData();

		//check if all fb specific information is available in the request
		if (!task.checkSocialConfigInfo()) {
			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), 
					"Facebook authentication token(s) are missing");
		}

		//check if all query parameters are missing in the query
		if (!(task.isToTrackAvailable() || task.isSubscribedProfilesAvailable())) {
			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), 
					"Missing subscribed profiles. At least one profile should be subscribed");
		}
		String collectionCode = task.getCollectionCode();

		//check if a task is already running with same configurations
		logger.info("Checking OAuth parameters for " + collectionCode);

		if (cache.isConfigExists(task)) {
			String msg = "Provided OAuth configurations already in use. Please stop this collection and then start again.";
			logger.info(collectionCode + ": " + msg);

			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), msg);
		}

		task.setFacebookStatus(CollectionStatus.RUNNING);
		logger.info("Initializing connection with Facebook for collection " + collectionCode);
		try {

			FacebookFeedTracker tracker = new FacebookFeedTracker(task);
			tracker.start();

			String code = task.getCollectionCode();
			cache.incrFbCounter(code, 0L);

			// if facebook connection successful then change the statuscode
			task.setFacebookStatus(CollectionStatus.RUNNING);
			task.setStatusMessage(null);
			cache.setFacebookTracker(code, tracker);
			cache.setFbConfigMap(code, task);

			//Adding a new log to CollectionLog
			collectionLogService.createByCollectionId(id, CollectionType.FACEBOOK);

			//Updating the status of collection in db
			collectionService.updateFacebookStatusAndLastExecutionTimeById(id, CollectionStatus.RUNNING, task.getLastExecutionTime());
			return new ResponseWrapper(cache.getFacebookConfig(code), true, CollectionStatus.RUNNING.toString(), 
					CollectionStatus.RUNNING.toString());
		} catch (Exception ex) {
			logger.error("Exception in creating Facebook Feed Tracker for collection " + collectionCode, ex);
			return new ResponseWrapper(null, false, CollectionStatus.FATAL_ERROR.toString(), ex.getMessage());
		}
	}

	
	@Override
	public ResponseWrapper getStatus(Long id) {

		Collection collection = collectionService.getById(id);

		String code = collection.getCode();
		CollectionTask failedTask = cache.getFailedCollectionTask(code);
		if (failedTask != null) {
			return stop(id);
		}

		CollectionTask facebookTask = cache.getFacebookConfig(code);
		if (facebookTask != null) {
			collectionLogService.updateCount(id, facebookTask.getFbPostCount(), CollectionType.FACEBOOK);

			if(collection.getFacebookStatus() != facebookTask.getFacebookStatus() && collection.getLastExecutionTime().before(facebookTask.getLastExecutionTime())){
				collectionService.updateFacebookStatusAndLastExecutionTimeById(id, facebookTask.getFacebookStatus(), facebookTask.getLastExecutionTime());
			}
			return new ResponseWrapper(facebookTask, true, ResponseCode.SUCCESS.toString(), null);
		}else if(collection.getFacebookStatus() != CollectionStatus.NOT_RUNNING){
			collection.setFacebookStatus(CollectionStatus.NOT_RUNNING);
			collectionService.updateFacebookStatusById(id, CollectionStatus.NOT_RUNNING);
		}
		return new ResponseWrapper(null, true, ResponseCode.SUCCESS.toString(), "Invalid key. No running collector found for the given id.");
	}


	@RequestMapping("/rerun")
	public ResponseWrapper rerunCollection(@RequestParam("id") Long id) {
		Collection collection = collectionService.getById(id);
		String code = collection.getCode();
		return rerunCollection(code);
	}
	
	
	public ResponseWrapper rerunCollection(String collectionCode) {
		if(cache.getFbConfigMap(collectionCode) != null) {
			FacebookFeedTracker tracker = cache.getFacebookTracker(collectionCode);
			tracker.start();
			logger.info("Collection " + collectionCode + " started.");
		}
		return new ResponseWrapper(cache.getFacebookConfig(collectionCode), true, CollectionStatus.RUNNING.toString(), 
				CollectionStatus.RUNNING.toString());
	}
	

	@Override
	public ResponseWrapper stop(Long id) {
		logger.info("Stopping the collection having collectionId: "+id);
		ResponseWrapper stopTaskResponse = stopTask(id);

		if(stopTaskResponse == null) {
			stopTaskResponse = new ResponseWrapper(null, true, CollectionStatus.NOT_RUNNING.toString(),
					"Invalid key. No running collector found for the given id.");
		}else{
			//Update the endDate and collection count in collectionLog
			collectionLogService.stop(id, ((CollectionTask) stopTaskResponse.getData()).getTweetCount(), CollectionType.FACEBOOK);
		}
		collectionService.updateFacebookStatusById(id, CollectionStatus.NOT_RUNNING);
		((CollectionTask) stopTaskResponse.getData()).setFacebookStatus(CollectionStatus.NOT_RUNNING);
		return stopTaskResponse;
	}

	
	protected ResponseWrapper stopTask(Long id) {
		Collection collection = collectionService.getById(id);
		String collectionCode = collection.getCode();
		CollectionTask task = null;

		FacebookFeedTracker tracker = cache.getFacebookTracker(collectionCode);

		if (tracker != null) {
			try {
				tracker.close();
				task = cache.getFacebookConfig(collectionCode);
				clearCache(collectionCode);
			} catch (IOException e) {
				task = cache.getFacebookConfig(collectionCode);
				clearCache(collectionCode);
				return new ResponseWrapper(task, true, CollectionStatus.NOT_RUNNING.toString(), e.getMessage());
			}
			logger.info(collectionCode + ": " + "Collector has been successfully stopped.");
		} else {
			task = cache.getFacebookConfig(collectionCode);
			clearCache(collectionCode);
			logger.info("No collector instances found to be stopped with the given id:" + id);
		}

		if (task != null) {
			return new ResponseWrapper(task, true, CollectionStatus.NOT_RUNNING.toString(), null);
		}
		return null;
	}


	@Override
	protected ResponseWrapper restart(Long id){
		logger.info("Stopping the facebook collection having collectionId: "+id);
		stop(id);
		logger.info("Starting the facebook collection having collectionId: "+id);
		return start(id);
	}


	@RequestMapping(value = "/searchProfiles", method={RequestMethod.POST})
	@ResponseBody
	public Object searchProfiles(@RequestParam String keyword, @RequestParam Integer limit, @RequestParam Integer offset) {
		Account authenticatedUser = util.getAuthenticatedUser();
		if(authenticatedUser != null){
			UserConnection userConnection = userConnectionService.getByProviderIdAndUserId(CollectionType.FACEBOOK.getValue(), authenticatedUser.getUserName());
			if(userConnection != null){
				String accessToken = userConnection.getAccessToken();
				FacebookFeedTracker tracker = new FacebookFeedTracker(accessToken);
				List<FacebookProfile> fbProfiles = tracker.searchProfiles(keyword, limit, offset);
				try {
					tracker.close();
				} catch (IOException e) {
					logger.error("Exception while closing the tracker", e);
				}
				return fbProfiles;
			}
		}
		return null;
	}


	private void clearCache(String collectionCode) {
		cache.delFbSyncObjMap(collectionCode);
		cache.delFailedCollection(collectionCode);
		cache.deleteFbPostCounter(collectionCode);
		cache.delFbConfigMap(collectionCode);
		cache.delFacebookTracker(collectionCode);
		cache.delReconnectAttempts(collectionCode);
	}

	@Override
	public ResponseWrapper getCount(Long id) {
		Long collectionCount = collectionLogService.getCountByCollectionIdAndProvider(id, CollectionType.FACEBOOK);
		return new ResponseWrapper(collectionCount, true, ResponseCode.SUCCESS.toString(), null);
	}
}
