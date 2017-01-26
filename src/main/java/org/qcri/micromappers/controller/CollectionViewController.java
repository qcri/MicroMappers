package org.qcri.micromappers.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.PageInfo;
import org.qcri.micromappers.service.CollaboratorService;
import org.qcri.micromappers.service.CollectionLogService;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.service.GlideMasterService;
import org.qcri.micromappers.service.GlobalEventDefinitionService;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Kushal
 */
@Controller
@RequestMapping("/collection/view")
public class CollectionViewController {
	protected static Logger logger = Logger.getLogger(CollectionViewController.class);
	
	@Autowired
	private CollectionService collectionService;
	@Autowired
	CollectionLogService collectionLogService;
	@Autowired
	CollaboratorService collaboratorService;
	@Autowired 
	private Util util;
    @Autowired
    GlobalEventDefinitionService globalEventDefinitionService;
    @Autowired
    GlideMasterService glideMasterService;

    
    /** This method is used to populate the paginated collection list for the authenticated user.
     * @param model
     * @param request
     * @return view of table collection/list/list.ftl
     */
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public String getAllCollections(Model model, HttpServletRequest request, 
    		@RequestParam(value = "page", defaultValue = "1") Integer page,
    		@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "sortColumn", required = false, defaultValue = "createdAt") String sortColumn,
			@RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Direction sortDirection) {
        
        Account account = util.getAuthenticatedUser();
        
        Page<Collection> pagedCollection =  collectionService.getAllByPage(account, page, pageSize, sortColumn, sortDirection);
        Page<CollectionDetailsInfo> pagedCollectionDetailsInfo = pagedCollection.map(pc -> pc.toCollectionDetailsInfo());
        
        PageInfo<CollectionDetailsInfo> pageInfo = new PageInfo<>(pagedCollectionDetailsInfo);
        pageInfo.setList(pagedCollectionDetailsInfo.getContent());

        model.addAttribute("page", pageInfo);
        return "collection/list/list";
    }
    
    
    /**
     * This method is used to display the form to create a new collection.
     * @param model
     * @param request
     * @return collection/create/create.ftl
     */
    @RequestMapping(value="/create", method = RequestMethod.GET)
	public String createCollection(Model model, HttpServletRequest request, @RequestParam(value = "type", required=false) String type,
			@RequestParam(value = "typeId", required=false) Long typeId){
    	
    	Account authenticatedUser = util.getAuthenticatedUser();
    	
    	if(StringUtils.isNotBlank(type) && typeId != null){
    		if(type.equalsIgnoreCase("snopes")){
        		GlobalEventDefinition globalEventDefinition = globalEventDefinitionService.getById(typeId);
        		Collection collection = collectionService.getByAccountAndGlobalEventDefinition(authenticatedUser, globalEventDefinition);
        		if(collection != null){
    				model.addAttribute("collectionId", collection.getId());
    				return "collection/create/alreadyExistsError";
    			}
        		model.addAttribute("keywords", globalEventDefinition.getArticleTag());
        		model.addAttribute("eventInfo", globalEventDefinition);
        	}
    		if(type.equalsIgnoreCase("gdelt")){
    			GlideMaster glideMaster = glideMasterService.getById(typeId);
    			Collection collection = collectionService.getByAccountAndGlideMaster(authenticatedUser, glideMaster);
    			if(collection != null){
    				model.addAttribute("collectionId", collection.getId());
    				return "collection/create/alreadyExistsError";
    			}
    			model.addAttribute("eventInfo", glideMaster);
    		}
    		
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
    @RequestMapping(value="/details", method = RequestMethod.GET)
	public String getCollectionDetails(Model model, HttpServletRequest request, @RequestParam("id") Long id){
    	
    	Collection collection = collectionService.getById(id);
    	CollectionDetailsInfo collectionDetailsInfo = null;
    	List<Account> collaborators = null;
    	Long twitterCount = 0L;
    	Long facebookCount = 0L;
    	if(collection != null){
    		collectionDetailsInfo = collection.toCollectionDetailsInfo();
    		collaborators = collaboratorService.getCollaboratorsByCollection(id);
    		
    		if(collection.getProvider() == CollectionType.ALL || collection.getProvider() == CollectionType.TWITTER){
    			twitterCount = collectionLogService.getCountByCollectionIdAndProvider(id, CollectionType.TWITTER);
    			model.addAttribute("twitterCount",twitterCount);
    		}
    		if(collection.getProvider() == CollectionType.ALL || collection.getProvider() == CollectionType.FACEBOOK){
    			facebookCount = collectionLogService.getCountByCollectionIdAndProvider(id, CollectionType.FACEBOOK);
    			model.addAttribute("facebookCount",facebookCount);
    		}
    	}
    	model.addAttribute("collectionInfo", collectionDetailsInfo);
    	model.addAttribute("collectionCreatedAt", collection.getCreatedAt());
    	
    	String collaboratorsString = collaborators.stream().map(c -> c.getUserName()).collect(Collectors.joining(", "));
    	model.addAttribute("collectionCollaborators",collaboratorsString);
    	
		return "collection/details/details";
	}
    
    @RequestMapping(value="/update", method = RequestMethod.GET)
	public String updateCollection(Model model, HttpServletRequest request, @RequestParam("id") Long id){
    	
    	Collection collection = collectionService.getById(id);
    	CollectionDetailsInfo collectionDetailsInfo = null;
    	if(collection != null){
    		collectionDetailsInfo = collection.toCollectionDetailsInfo();
    	}
    	model.addAttribute("collectionInfo", collectionDetailsInfo);
    	
		return "collection/update/update";
	}

}
