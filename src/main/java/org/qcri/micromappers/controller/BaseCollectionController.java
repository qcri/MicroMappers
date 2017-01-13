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
			logger.error(e.getMessage(), e);
			return new ResponseWrapper(null, false, "Failure", "Error while creating a new collection : "+ e.getMessage());
		}

		if(collection == null) {
			return new ResponseWrapper(null, false, "Failure", "Collection not created");
		}

		//Running collection right after creation
		if (runAfterCreate && collection != null) {
			return start(collection.getId());
		} 

		return new ResponseWrapper(collection.toCollectionDetailsInfo(), true, "Successful", "Collection created Successfully");
	}

	@RequestMapping(value = "/start", method=RequestMethod.GET)
	@ResponseBody
	protected ResponseWrapper start(@RequestParam Long id) {
		
		logger.info("Starting the collection having collectionId: "+id);
		ResponseWrapper preparedCollectionTask = baseCollectionService.prepareCollectionTask(id);
		if(preparedCollectionTask.getSuccess()){
			return startTask((CollectionTask) preparedCollectionTask.getData());
		}
		return preparedCollectionTask;
	}
	
	public abstract ResponseWrapper startTask(CollectionTask collectionTask);

	@RequestMapping(value = "/stop", method=RequestMethod.GET)
	public ResponseWrapper stop(@RequestParam  Long id) {
		logger.info("Stopping the collection having collectionId: "+id);
		ResponseWrapper stopTaskResponse = stopTask(id);

		if(stopTaskResponse == null) {
			stopTaskResponse = new ResponseWrapper(null, true, CollectionStatus.NOT_RUNNING.toString(),
					"Invalid key. No running collector found for the given id.");
		}else{
			//Update the endDate and collection count in collectionLog
			collectionLogService.stop(id, ((CollectionTask) stopTaskResponse.getData()).getCollectionCount());
		}
		collectionService.updateStatusById(id, CollectionStatus.NOT_RUNNING);

		return stopTaskResponse;
	}
	
	protected abstract ResponseWrapper stopTask(Long id);

	@RequestMapping(value = "/status", method=RequestMethod.GET)
	public ResponseWrapper getStatus(@RequestParam("id") Long id) {

		Collection collection = collectionService.getById(id);

		GenericCache cache = GenericCache.getInstance();
		CollectionTask task = cache.getTwitterConfig(collection.getCode());
		if (task != null) {
			collectionLogService.updateCount(id, task.getCollectionCount());
			
			if(collection.getStatus() != task.getStatusCode()){
				collectionService.updateStatusByCode(collection.getCode(), task.getStatusCode());
			}
			
			return new ResponseWrapper(task, true, task.getStatusCode().toString(), task.getStatusMessage());
		}

		CollectionTask failedTask = cache.getFailedCollectionTask(collection.getCode());
		if (failedTask != null) {
			collectionLogService.updateCount(id, failedTask.getCollectionCount());
			
			if(collection.getStatus() != failedTask.getStatusCode()){
				collectionService.updateStatusByCode(collection.getCode(), failedTask.getStatusCode());
			}
			
			return new ResponseWrapper(failedTask, true, failedTask.getStatusCode().toString(), failedTask.getStatusMessage());
		}

		if(collection.getStatus() != CollectionStatus.NOT_RUNNING){
			collectionService.updateStatusByCode(collection.getCode(), CollectionStatus.NOT_RUNNING);
		}
		return new ResponseWrapper(null, true, CollectionStatus.NOT_RUNNING.toString(), "Invalid key. No running collector found for the given id.");
	}
	

    @RequestMapping(value = "/restart", method=RequestMethod.GET)
    protected ResponseWrapper restart(@RequestParam("id") Long id){
		logger.info("Stopping the collection having collectionId: "+id);
    	stop(id);
    	logger.info("Starting the collection having collectionId: "+id);
		return start(id);
    }
    

    /** Updating the collection
     * @param collectionDetailsInfo
     * @param id is collectionId
     * @return
     */
    @RequestMapping(value = "/update", method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper updateCollection(@RequestBody CollectionDetailsInfo collectionDetailsInfo, 
			@RequestParam("id") Long id) {

    	ResponseWrapper response = null;
		try{
			logger.info("Updating collection with collectionId: "+ id);
			collectionDetailsInfo.setId(id);
			response = baseCollectionService.update(collectionDetailsInfo);
		}catch (MicromappersServiceException e) {
			logger.error("Error while updating collection"+ e.getMessage(), e);
			return new ResponseWrapper(null, false, "Failure", "Error while updating collection : "+ e.getMessage());
		}

		if(response.getSuccess() != null || response.getSuccess() == Boolean.TRUE){
			CollectionDetailsInfo collectionInfo = (CollectionDetailsInfo) response.getData();
			
			ResponseWrapper status = getStatus(collectionInfo.getId());
			String statusCode = status.getStatusCode();
			
			if(statusCode.equals(CollectionStatus.RUNNING.toString()) || 
					statusCode.equals(CollectionStatus.RUNNING_WARNING.toString()) || 
					statusCode.equals(CollectionStatus.WARNING.toString())){
				return restart(collectionInfo.getId());
			}
		}
		return response;
	}
}
