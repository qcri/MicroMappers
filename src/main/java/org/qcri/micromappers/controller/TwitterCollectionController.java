package org.qcri.micromappers.controller;

import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.utility.GenericCache;
import org.qcri.micromappers.utility.ResponseWrapper;
import org.qcri.micromappers.utility.TwitterStreamTracker;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Kushal
 * RESTFul APIs to start and stop Twitter collections.
 * TODO: remove non-API related operations such as startPersister to other appropriate classes.
 */
@RestController
@RequestMapping("/twitter")
public class TwitterCollectionController extends BaseCollectionController {


	public ResponseWrapper startTask(CollectionTask task) {
		logger.info("Collection start request received for " + task.getCollectionCode());

		//check if all twitter specific information is available in the request
		if (!task.checkSocialConfigInfo()) {
			return new ResponseWrapper(null, false, 
					configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR), 
					"One or more Twitter authentication token(s) are missing");
		}

		//check if all query parameters are missing in the query
		if (!task.isToTrackAvailable() && !task.isToFollowAvailable() && !task.isGeoLocationAvailable()) {
			return new ResponseWrapper(null, false, 
					configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR), 
					"Missing one or more fields (toTrack, toFollow, and geoLocation). At least one field is required");
		}

		String collectionCode = task.getCollectionCode();

		//check if a task is already running with same configurations
		logger.info("Checking OAuth parameters for " + collectionCode);
		GenericCache cache = GenericCache.getInstance();
		if (cache.isConfigExists(task)) {
			String msg = "Provided OAuth configurations already in use. Please stop this collection and then start again.";
			logger.info(collectionCode + ": " + msg);

			return new ResponseWrapper(null, false, 
					configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR), msg);
		}

		logger.info("Initializing connection with Twitter streaming API for collection " + collectionCode);
		task.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));

		try {
			TwitterStreamTracker tracker = new TwitterStreamTracker(task);
			tracker.start();

			String code = task.getCollectionCode();
			cache.incrCounter(code, new Long(0));

			// if twitter streaming connection successful then change the status code
			task.setStatusCode(configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_RUNNING));
			task.setStatusMessage(null);
			cache.setTwitterTracker(code, tracker);
			cache.setTwtConfigMap(code, task);

			return new ResponseWrapper(null, true, 
					configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING), 
					configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_INITIALIZING));
		} catch (Exception ex) {
			logger.error("Exception in creating TwitterStreamTracker for collection " + collectionCode, ex);
			return new ResponseWrapper(null, false, 
					configProperties.getProperty(MicromappersConfigurationProperty.STATUS_CODE_COLLECTION_ERROR), 
					ex.getMessage());
		}
	}

}
