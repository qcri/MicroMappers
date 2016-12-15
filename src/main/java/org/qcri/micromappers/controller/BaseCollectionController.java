package org.qcri.micromappers.controller;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.service.BaseCollectionService;
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
    	CollectionTask collectionTask = baseCollectionService.prepareCollectionTask(id);
    	return startTask(collectionTask);
	}
    
    public abstract ResponseWrapper startTask(CollectionTask collectionTask);
    
//    @RequestMapping("/stop")
//    protected abstract Response stopCollection(@RequestParam("id") String collectionCode);
//    
//    @RequestMapping("/status")
//    protected abstract Response getStatus(@RequestParam("id") String id);
//    
//    @RequestMapping("/restart")
//    protected abstract ResponseWrapper restartCollection(@QueryParam("code") String collectionCode);
//    
   
//    
    /*public Response stopTask(@RequestParam("id") String collectionCode) {
        
        Response stopTaskResponse = stopCollection(collectionCode);
        
        if(stopTaskResponse == null) {
	        ResponseWrapper response = new ResponseWrapper();
	        response.setMessage("Invalid key. No running collector found for the given id.");
	        response.setStatusCode(configProperties.getProperty(CollectorConfigurationProperty.STATUS_CODE_COLLECTION_NOTFOUND));
	        stopTaskResponse = Response.ok(response).build();
        } 
       
        return stopTaskResponse;
    }
    
    @RequestMapping("/status/all")
    public List<CollectionTask> getStatusAll() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllConfigs();
        return allTasks;
    }

    @RequestMapping("/failed/all")
    public List<CollectionTask> getAllFailedCollections() {
        List<CollectionTask> allTasks = GenericCache.getInstance().getAllFailedCollections();
        return allTasks;
    }
    
    
    protected void startPersister(String collectionCode, boolean saveMediaEnabled) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_REST_URI) 
            		+ "collectionPersister/start?channel_provider="
                    + URLEncoder.encode(configProperties.getProperty(CollectorConfigurationProperty.TAGGER_CHANNEL), "UTF-8")
                    + "&collection_code=" + URLEncoder.encode(collectionCode, "UTF-8")
                    + "&saveMediaEnabled=" + saveMediaEnabled);
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);

            logger.info(collectionCode + ": Collector persister response = " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not start persister. Is persister running?", e);
            CollectorErrorLog.sendErrorMail(collectionCode, "Unable to start persister. Is persister running");
        } catch (UnsupportedEncodingException e) {
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }

    public void stopPersister(String collectionCode) {
        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        try {
            WebTarget webResource = client.target(configProperties.getProperty(CollectorConfigurationProperty.PERSISTER_REST_URI)
                    + "collectionPersister/stop?collection_code=" + URLEncoder.encode(collectionCode, "UTF-8"));
            Response clientResponse = webResource.request(MediaType.APPLICATION_JSON).get();
            String jsonResponse = clientResponse.readEntity(String.class);
            logger.info(collectionCode + ": Collector persister response =  " + jsonResponse);
        } catch (RuntimeException e) {
            logger.error(collectionCode + ": Could not stop persister. Is persister running?", e);
        } catch (UnsupportedEncodingException e) {
            logger.error(collectionCode + ": Unsupported Encoding scheme used");
        }
    }*/
}
