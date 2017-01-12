package org.qcri.micromappers.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.AccountDTO;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.CollaboratorService;
import org.qcri.micromappers.service.CollectionLogService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.ResponseCode;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kushal
 */
@Controller
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
	private Util util;
	
	@Autowired
	private BaseCollectionController baseCollectionController;

    @Autowired
    GlobalEventDefinitionService globalEventDefinitionService;


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

	/* @RequestMapping("/status/all")
    public List<CollectionTask> getStatusAll() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllConfigs();
        return allTasks;
    }*/
    
    /** Used to test whether the collection with the given name already exists or not.
     * @param name
     * @return json having valid=true/false
     * @throws Exception
     */
    @RequestMapping(value = "/existName.action", method = RequestMethod.GET)
	@ResponseBody
	public Object existName(@RequestParam String name) throws Exception {
    	boolean collectionNameExists = collectionService.isCollectionNameExists(name.trim().toLowerCase());
    	JSONObject response = new JSONObject();
    	response.put("valid", !collectionNameExists);
    	return response;
	}
    
    /** Returning the collection count from db.
     * @param id
     * @return
     */
    @RequestMapping(value = "/count", method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper getCount(@RequestParam("id") Long id) {

		Long collectionCount = collectionLogService.getCountByCollectionId(id);
		return new ResponseWrapper(collectionCount, true, ResponseCode.SUCCESS.toString(), null);
	}
    
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper delete(@RequestParam("id") Long id) throws Exception {
		logger.info("Deleting Collection having id: "+id);
		ResponseWrapper status = baseCollectionController.getStatus(id);
		String statusCode = status.getStatusCode();
		
		if(statusCode.equals(CollectionStatus.RUNNING.toString()) || 
				statusCode.equals(CollectionStatus.RUNNING_WARNING.toString()) || 
				statusCode.equals(CollectionStatus.WARNING.toString())){
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.SUCCESS.toString(), "Please stop the collection before deleting.");
		}
		
		Boolean isDeleted = collectionService.delete(id);
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
		
		Boolean isRestored = collectionService.untrash(id);
		if(isRestored){
			return new ResponseWrapper(null, Boolean.TRUE, ResponseCode.SUCCESS.toString(), "Collection restored successfully.");
		}else{
			return new ResponseWrapper(null, Boolean.FALSE, ResponseCode.SUCCESS.toString(), "Error while restoring collection.");
		}
	}
    
    
    /** This method is used to populate the paginated collection list for the authenticated user.
     * @param model
     * @param request
     * @return view of table collection/list/list.ftl
     */
    @RequestMapping(value="/view/list", method = RequestMethod.GET)
    public String getAllCollections(Model model, HttpServletRequest request, 
    		@RequestParam(value = "page", defaultValue = "1") String page) {
        int pageNumber = Integer.valueOf(page);
        
        Account account = util.getAuthenticatedUser();
        
        Page<Collection> pages =  collectionService.getAllByPage(account, pageNumber);

        PageInfo<Collection> pageInfo = new PageInfo<>(pages);
        pageInfo.setList(pages.getContent());

        model.addAttribute("page", pageInfo);
        return "collection/list/list";
    }
    
    
    /**
     * This method is used to display the form to create a new collection.
     * @param model
     * @param request
     * @return collection/create/create.ftl
     */
    @RequestMapping(value="/view/create", method = RequestMethod.GET)
	public String createCollection(Model model, HttpServletRequest request, @RequestParam(value = "type", required=false) String type,
			@RequestParam(value = "typeId", required=false) Long typeId){
    	
    	if(StringUtils.isNotBlank(type) && typeId != null){
    		if(type.equals("snopes")){
        		GlobalEventDefinition globalEventDefinition = globalEventDefinitionService.getById(typeId);
        		model.addAttribute("keywords", globalEventDefinition.getArticleTag());
        		model.addAttribute("eventTitle", WordUtils.capitalize(type) + ": " +globalEventDefinition.getTitle());
        	}
    		/*if(type.equals("gdelt")){
    		}*/
    		
    		model.addAttribute("eventType", type);
        	model.addAttribute("eventTypeId", typeId);
    	}
    	
		return "collection/create/create";
	}
    
    
    /**This method is used to display the details of a collection.
     * @param model
     * @param request
     * @param id
     * @return collection/details/details
     */
    @RequestMapping(value="/view/details", method = RequestMethod.GET)
	public String getCollectionDetails(Model model, HttpServletRequest request, @RequestParam("id") Long id){
    	
    	Collection collection = collectionService.getById(id);
    	CollectionDetailsInfo collectionDetailsInfo = null;
    	List<Account> collaborators = null;
    	Long collectionCount = 0L;
    	if(collection != null){
    		collectionDetailsInfo = collection.toCollectionDetailsInfo();
    		collaborators = collaboratorService.getCollaboratorsByCollection(id);
    		collectionCount = collectionLogService.getCountByCollectionId(id);
    		if(collectionDetailsInfo.getGlobalEventDefinitionId() != null){
    			GlobalEventDefinition globalEventDefinition = globalEventDefinitionService.getById(collectionDetailsInfo.getGlobalEventDefinitionId());
    			model.addAttribute("eventTitle", "Snopes: " + globalEventDefinition.getTitle());
    		}
    	}
    	model.addAttribute("collectionInfo", collectionDetailsInfo);
    	model.addAttribute("collectionCreatedAt", collection.getCreatedAt());
    	
    	String collaboratorsString = collaborators.stream().map(c -> c.getUserName()).collect(Collectors.joining(","));
    	model.addAttribute("collectionCollaborators",collaboratorsString);
    	model.addAttribute("collectionCount",collectionCount);
		return "collection/details/details";
	}
    
    @RequestMapping(value="/view/update", method = RequestMethod.GET)
	public String updateCollection(Model model, HttpServletRequest request, @RequestParam("id") Long id){
    	
    	Collection collection = collectionService.getById(id);
    	CollectionDetailsInfo collectionDetailsInfo = null;
    	if(collection != null){
    		collectionDetailsInfo = collection.toCollectionDetailsInfo();
    		if(collectionDetailsInfo.getGlobalEventDefinitionId() != null){
    			GlobalEventDefinition globalEventDefinition = globalEventDefinitionService.getById(collectionDetailsInfo.getGlobalEventDefinitionId());
    			model.addAttribute("eventTitle", "Snopes: " + globalEventDefinition.getTitle());
    		}
    	}
    	model.addAttribute("collectionInfo", collectionDetailsInfo);
    	
		return "collection/update/update";
	}

}
