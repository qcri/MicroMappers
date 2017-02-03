package org.qcri.micromappers.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
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
import org.qcri.micromappers.service.GlideMasterService;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kushal
 */
@RestController
@RequestMapping("/collection")
public class CollectionController {
	protected static Logger logger = Logger.getLogger(CollectionController.class);

	@Autowired
	private CollectionService collectionService;
	@Autowired
	CollectionLogService collectionLogService;
	@Autowired
	CollaboratorService collaboratorService;
	@Autowired
	private BaseCollectionService baseCollectionService;
	@Autowired
	TwitterCollectionController twitterCollectionController;
	@Autowired
	FacebookCollectionController facebookCollectionController;
	@Autowired
	GlobalEventDefinitionService globalEventDefinitionService;
	@Autowired
	GlideMasterService glideMasterService;


	@RequestMapping(value = "/create", method=RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper createCollection(@RequestBody CollectionDetailsInfo collectionDetailsInfo,
			@RequestParam(value = "runAfterCreate", defaultValue = "false", required = false)
	Boolean runAfterCreate) {

		Collection collection = null;
		try{
			logger.info("Creating a new collection with collectionCode: "+ collectionDetailsInfo.getCode());
			collection = baseCollectionService.create(collectionDetailsInfo);
		}catch (MicromappersServiceException e) {
			logger.error(e.getMessage(), e);
			return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Error while creating a new collection : "+ e.getMessage());
		}

		if(collection == null) {
			return new ResponseWrapper(null, false, ResponseCode.FAILED.toString(), "Collection not created");
		}
		logger.info("New collection created with collectionCode : "+ collectionDetailsInfo.getCode());

		//Running collection right after creation
		if (runAfterCreate && collection != null) {

			Long collectionId = collection.getId();
			switch(collection.getProvider()){
			case TWITTER	: 	twitterCollectionController.start(collectionId);
			break;
			case FACEBOOK	: 	facebookCollectionController.start(collectionId);
			break;
			case ALL		:	twitterCollectionController.start(collectionId);
			facebookCollectionController.start(collectionId);
			break;
			}

			collection = collectionService.getById(collectionId);
		} 

		return new ResponseWrapper(collection.toCollectionDetailsInfo(), true, ResponseCode.SUCCESS.toString(), "Collection created Successfully");
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

			ResponseWrapper status = getCollectionStatus(id);

			if(status.getData() != null){
				CollectionTask collectionTask = (CollectionTask) status.getData();

				if(collectionTask.getTwitterStatus().equals(CollectionStatus.RUNNING.toString()) || 
						collectionTask.getTwitterStatus().equals(CollectionStatus.RUNNING_WARNING.toString()) || 
						collectionTask.getTwitterStatus().equals(CollectionStatus.WARNING.toString())){
					
					twitterCollectionController.restart(collectionInfo.getId());
				}
				if(collectionTask.getFacebookStatus().equals(CollectionStatus.RUNNING.toString()) || 
						collectionTask.getFacebookStatus().equals(CollectionStatus.RUNNING_WARNING.toString()) || 
						collectionTask.getFacebookStatus().equals(CollectionStatus.WARNING.toString())){
					
					facebookCollectionController.restart(collectionInfo.getId());
				}
			}
		}
		return response;
	}


	@RequestMapping(value = "/status", method=RequestMethod.GET)
	public ResponseWrapper getCollectionStatus(@RequestParam("id") Long id) {

		Collection collection = collectionService.getById(id);
		CollectionTask twitterTask = null;
		CollectionTask facebookTask = null;

		if(collection.getProvider() == CollectionType.ALL || collection.getProvider() == CollectionType.TWITTER){
			ResponseWrapper twitterResponse = twitterCollectionController.getStatus(id);

			if(twitterResponse != null && twitterResponse.getData() != null){
				twitterTask = (CollectionTask) twitterResponse.getData();
			}
		}
		if(collection.getProvider() == CollectionType.ALL || collection.getProvider() == CollectionType.FACEBOOK){
			ResponseWrapper facebookResponse = facebookCollectionController.getStatus(id);
			if(facebookResponse != null && facebookResponse.getData() != null){
				facebookTask = (CollectionTask) facebookResponse.getData();
			}
		}

		if(twitterTask != null){
			if(facebookTask != null){
				twitterTask.setFacebookStatus(facebookTask.getFacebookStatus());
			}

			twitterTask.setFacebookStatus(collection.getFacebookStatus());
			return new ResponseWrapper(twitterTask, true, ResponseCode.SUCCESS.toString(), null);
		}else if(facebookTask != null){
			facebookTask.setTwitterStatus(collection.getTwitterStatus());
			return new ResponseWrapper(facebookTask, true, ResponseCode.SUCCESS.toString(), null);
		}

		return new ResponseWrapper(null, true, ResponseCode.SUCCESS.toString(), "Invalid key. No running collector found for the given id.");
	}

	/** It is used to fetch the list of collaborators for a collection. 
	 * @param id
	 * @return Json ResponseWrapper.
	 */
	@RequestMapping(value = "/collaborators", method=RequestMethod.GET)
	@ResponseBody
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


	/** Adding a collaborator to a collection.
	 * @param id
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
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

	/** It is used to remove a collaborator from a collection.
	 * @param id
	 * @param accountId
	 * @return	Json ResponseWrapper
	 * @throws Exception
	 */
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


	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper delete(@RequestParam("id") Long id) throws Exception {
		logger.info("Deleting Collection having id: "+id);
		ResponseWrapper status = getCollectionStatus(id);
		CollectionTask task = (CollectionTask) status.getData();
		
		
		if(	task != null && 
				(task.getTwitterStatus().equals(CollectionStatus.RUNNING.toString()) || 
					task.getTwitterStatus().equals(CollectionStatus.RUNNING_WARNING.toString()) || 
					task.getTwitterStatus().equals(CollectionStatus.WARNING.toString()) ||
					
					task.getFacebookStatus().equals(CollectionStatus.RUNNING.toString()) || 
					task.getFacebookStatus().equals(CollectionStatus.RUNNING_WARNING.toString()) || 
					task.getFacebookStatus().equals(CollectionStatus.WARNING.toString()))
			){
				return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.SUCCESS.toString(), "Please stop the collection before deleting.");
		}

		Boolean isDeleted = collectionService.trashCollectionById(id);
		if(isDeleted){
			return new ResponseWrapper(null, Boolean.TRUE, ResponseCode.SUCCESS.toString(), "Collection deleted successfully.");
		}else{
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.SUCCESS.toString(), "Error while deleting collection.");
		}
	}


	@RequestMapping(value = "/untrash", method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper untrash(@RequestParam("id") Long id) throws Exception {
		logger.info("Restoring Collection having id: "+id);

		Boolean isRestored = collectionService.untrashCollectionById(id);
		if(isRestored){
			return new ResponseWrapper(null, Boolean.TRUE, ResponseCode.SUCCESS.toString(), "Collection restored successfully.");
		}else{
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.SUCCESS.toString(), "Error while restoring collection.");
		}
	}


	/** Used to test whether the collection with the given name already exists or not.
	 * @param name
	 * @return json having valid=true/false
	 * @throws Exception
	 */
	@RequestMapping(value = "/existName", method = RequestMethod.GET)
	@ResponseBody
	public Object existName(@RequestParam String name) throws Exception {
		boolean collectionNameExists = collectionService.isCollectionNameExists(name.trim().toLowerCase());
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("valid", !collectionNameExists);
		return jsonResponse;
	}
}
