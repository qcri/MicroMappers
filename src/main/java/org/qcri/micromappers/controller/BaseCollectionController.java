package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.service.BaseCollectionService;
import org.qcri.micromappers.service.CollectionLogService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.GenericCache;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseCollectionController {

	protected static Logger logger = Logger.getLogger(BaseCollectionController.class);
	protected static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

	@Autowired
	BaseCollectionService baseCollectionService;

	@Autowired
	CollectionService collectionService;

	@Autowired
	CollectionLogService collectionLogService;

	@RequestMapping(value = "/create", method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper createCollection(@RequestBody CollectionDetailsInfo collectionDetailsInfo,
			@RequestParam(value = "runAfterCreate", defaultValue = "false", required = false)
	Boolean runAfterCreate) {

		Collection collection = null;
		try{
			logger.info("Creating a new collection with collectionCode: "+ collectionDetailsInfo.getCode());
			collection = baseCollectionService.create(collectionDetailsInfo);
			logger.info("New collection created with collectionCode : "+ collectionDetailsInfo.getCode());
		}catch (MicromappersServiceException e) {
			logger.error("Error while creating a new collection"+ e.getMessage(), e);
			return new ResponseWrapper(null, false, "Failure", "Error while creating a new collection : "+ e.getMessage());
		}

		if(collection == null) {
			return new ResponseWrapper(null, false, "Failure", "Collection not created");
		}

		//Running collection right after creation
		if (runAfterCreate && collection != null) {
			return start(collection.getId());
		} 

		return new ResponseWrapper(collection, true, "Successful", "Collection created Successfully");
	}

	@RequestMapping(value = "/start", method=RequestMethod.GET)
	@ResponseBody
	protected ResponseWrapper start(@RequestParam Long id) {
		//    	Check if the current user can start this collection
		Boolean permitted = baseCollectionService.isCurrentUserPermitted(id);
		if(permitted == null || !permitted){
			return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Current User is not permitted to execute this task.");
		}

		ResponseWrapper response = baseCollectionService.prepareCollectionTask(id);
		if(response.getSuccess()){
			return startTask((CollectionTask) response.getData());
		}
		return response;
	}

	public abstract ResponseWrapper startTask(CollectionTask collectionTask);

	@RequestMapping("/stop")
	public ResponseWrapper stop(@RequestParam  Long id) {
		//Check if the current user can stop this collection
		Boolean permitted = baseCollectionService.isCurrentUserPermitted(id);
		if(permitted == null || !permitted){
			return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Current User is not permitted to execute this task.");
		}

		ResponseWrapper stopTaskResponse = stopTask(id);

		if(stopTaskResponse == null) {
			stopTaskResponse = new ResponseWrapper(null, true, CollectionStatus.NOT_RUNNING.toString(),
					"Invalid key. No running collector found for the given id.");
		}else{
			//Update the endDate and colleciton count in collectionLog
			collectionLogService.stop(id, ((CollectionTask) stopTaskResponse.getData()).getCollectionCount());
		}
		collectionService.updateStatusById(id, CollectionStatus.NOT_RUNNING);

		return stopTaskResponse;
	}

	protected abstract ResponseWrapper stopTask(Long id);

	@RequestMapping("/status")
	public ResponseWrapper getStatus(@RequestParam("id") Long id) {

		Collection collection = collectionService.getById(id);

		GenericCache cache = GenericCache.getInstance();
		CollectionTask task = cache.getTwitterConfig(collection.getCode());
		if (task != null) {
			collectionLogService.updateCount(id, task.getCollectionCount());
			collectionService.updateStatusByCode(collection.getCode(), task.getStatusCode());
			return new ResponseWrapper(task, true, task.getStatusCode().toString(), task.getStatusMessage());
		}

		CollectionTask failedTask = cache.getFailedCollectionTask(collection.getCode());
		if (failedTask != null) {
			collectionLogService.updateCount(id, failedTask.getCollectionCount());
			collectionService.updateStatusByCode(collection.getCode(), failedTask.getStatusCode());
			return new ResponseWrapper(failedTask, true, failedTask.getStatusCode().toString(), failedTask.getStatusMessage());
		}

		collectionService.updateStatusByCode(collection.getCode(), CollectionStatus.NOT_RUNNING);
		return new ResponseWrapper(null, true, CollectionStatus.NOT_RUNNING.toString(), "Invalid key. No running collector found for the given id.");
	}

	//    @RequestMapping("/restart")
	//    protected abstract ResponseWrapper restartCollection(@QueryParam("code") String collectionCode);


	/* @RequestMapping("/status/all")
    public List<CollectionTask> getStatusAll() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllConfigs();
        return allTasks;
    }*/
	/* 
    @RequestMapping("/failed/all")
    public List<CollectionTask> getAllFailedCollections() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllFailedCollections();
        return allTasks;
    }
	 */
}
