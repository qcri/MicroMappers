package org.qcri.micromappers.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.AccountDTO;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.service.BaseCollectionService;
import org.qcri.micromappers.service.CollaboratorService;
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
	
	@Autowired
	CollaboratorService collaboratorService;

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
			//Update the endDate and colleciton count in collectionLog
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
    

    @RequestMapping(value = "/update", method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper updateCollection(@RequestBody CollectionDetailsInfo collectionDetailsInfo) {

    	ResponseWrapper response = null;
		try{
			logger.info("Updating collection with collectionCode: "+ collectionDetailsInfo.getCode());
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
    
    
    @RequestMapping(value = "/collaborators", method=RequestMethod.GET)
	public ResponseWrapper getCollaborators(@RequestParam("id") Long id) {

		List<Account> collaborators = null;
		try{
			collaborators = collaboratorService.getCollaboratorsByCollection(id);
		}catch (MicromappersServiceException e) {
			logger.error("Exception while fetching collaborators for the collectionId: "+id);
		}
		
		if(collaborators == null){
			return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), null);
		}
		List<AccountDTO> collaboratorsDto = collaborators.stream().map(c -> c.toDTO()).collect(Collectors.toList());
		return new ResponseWrapper(collaboratorsDto, true, ResponseCode.SUCCESS.toString(), null);
	}
    
    
    @RequestMapping(value = "/addCollaborator", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseWrapper addCollaborator(@RequestParam("id") Long id, @RequestParam Long accountId) throws Exception {
        logger.info("Adding collaborator to Collection");
        String msg = "Error while adding collaborator to collection.";
        try{
            if (id == null || accountId == null){
            	return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
            }
            
            Boolean success = collaboratorService.addCollaborator(id, accountId);

            if(success != null && success == Boolean.TRUE) {
            	return new ResponseWrapper(null, true, ResponseCode.SUCCESS.toString(), null);
            }
        	return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
        }catch(Exception e){
            logger.error(msg, e);
            return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
        }
    }
    
    @RequestMapping(value = "/removeCollaborator", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseWrapper removeCollaborator(@RequestParam("id") Long id, @RequestParam Long accountId) throws Exception {
        logger.info("Removing collaborator from Collection");
        String msg = "Error while removing collaborator to collection.";
        try{
            if (id == null || accountId == null){
            	return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
            }
            
            Boolean success = collaboratorService.removeCollaborator(id, accountId);

            if(success != null && success == Boolean.TRUE) {
            	return new ResponseWrapper(null, true, ResponseCode.SUCCESS.toString(), null);
            }
        	return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
        }catch(Exception e){
            logger.error(msg, e);
            return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), msg);
        }
    }

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
