package org.qcri.micromappers.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.qcri.micromappers.utility.persister.ZipDirectory;
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
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();


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
    public String getAllCollections(Model model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", defaultValue = "") String id) {

		if(id != null){
			if(!id.isEmpty())
			{
				this.downloadZipFile(response, Long.valueOf(id));
			}
		}
		Account account = util.getAuthenticatedUser();

		List<Collection> pagedCollection = collectionService.getAllByAccount(account);

		List<CollectionDetailsInfo> pagedCollectionDetailsInfo = new ArrayList<CollectionDetailsInfo>();
		pagedCollection.forEach(item->pagedCollectionDetailsInfo.add(item.toCollectionDetailsInfo()));

        model.addAttribute("page", pagedCollectionDetailsInfo);
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

	private void downloadZipFile(HttpServletResponse response, long id){
		Collection collection = collectionService.getById(id);

		response.setContentType("application/zip");
		String reportName = collection.getCode() + "_" + new Date().getTime() +".zip";
		response.setHeader("Content-disposition", "attachment;filename="+reportName);

		try {
			Path parentPath = Paths.get(configProperties.getProperty(MicromappersConfigurationProperty.Feed_PATH),
					collection.getCode());

			ZipDirectory zipDirectory = new ZipDirectory(reportName, parentPath.toString());
			zipDirectory.generateZipFile(response);
		} catch(Exception e){
			logger.error(e.getMessage());
		}
	}
}
