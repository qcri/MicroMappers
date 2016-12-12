package org.qcri.micromappers.controller;

import org.apache.commons.lang3.StringUtils;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Kushal
 * RESTFul APIs to start and stop Twitter collections.
 * TODO: remove non-API related operations such as startPersister to other appropriate classes.
 */
@RestController
@RequestMapping("/twitter")
public class TwitterCollectionController extends BaseCollectionController {

	
	
    /*@RequestMapping(value = "/start", method={RequestMethod.POST})
    public ResponseWrapper startTask(@RequestBody TwitterCollectionTask task) {
        logger.info("Collection start request received for " + task.getCollectionCode());
        logger.info("Details:\n" + task.toString());
        ResponseWrapper response = new ResponseWrapper();

        //check if all twitter specific information is available in the request
        if (!task.checkSocialConfigInfo()) {
            response.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
            response.setMessage("One or more Twitter authentication token(s) are missing");
            return response;
        }

        //check if all query parameters are missing in the query
        if (!task.isToTrackAvailable() && !task.isToFollowAvailable() && !task.isGeoLocationAvailable()) {
            response.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
            response.setMessage("Missing one or more fields (toTrack, toFollow, and geoLocation). At least one field is required");
            return response;
        }
        String collectionCode = task.getCollectionCode();

        //check if a task is already running with same configurations
        logger.info("Checking OAuth parameters for " + collectionCode);
        GenericCache cache = GenericCache.getInstance();
		if (cache.isConfigExists(task)) {
            String msg = "Provided OAuth configurations already in use. Please stop this collection and then start again.";
            logger.info(collectionCode + ": " + msg);
            response.setMessage(msg);
            response.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
            return response;
        }

		task.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
		logger.info("Initializing connection with Twitter streaming API for collection " + collectionCode);
		try {
//			TwitterStreamTracker tracker = new TwitterStreamTracker(task);
//			tracker.start();

			String cacheKey = task.getCollectionCode();
			cache.incrCounter(cacheKey, new Long(0));

			// if twitter streaming connection successful then change the status
			// code
			task.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
			task.setStatusMessage(null);
//			cache.setTwitterTracker(cacheKey, tracker);
			cache.setTwtConfigMap(cacheKey, task);
			if(task.getPersist()!=null){
				if(task.getPersist()){
					startPersister(collectionCode, task.isSaveMediaEnabled());
				}
			}
			else{
				if (Boolean.valueOf(configProperties.getProperty(MicromappersConfigurationProperty.DEFAULT_PERSISTANCE_MODE))) {
					startPersister(collectionCode, task.isSaveMediaEnabled());
				}
			}
			response.setMessage(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
			response.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
		} catch (Exception ex) {
			logger.error("Exception in creating TwitterStreamTracker for collection " + collectionCode, ex);
			response.setMessage(ex.getMessage());
			response.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR));
		}
		return response;
	}
*/

}
