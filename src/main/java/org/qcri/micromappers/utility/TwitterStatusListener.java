package org.qcri.micromappers.utility;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Predicate;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.MicroMappersApplication;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.service.DataFeedService;
import org.qcri.micromappers.service.EmailService;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;

import twitter4j.ConnectionLifeCycleListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

/**
 * This class is responsible for dispatching incoming tweets.
 * First it invokes all the filters which must be specified at
 * creation time. In case all the filters return TRUE for
 * given tweet, this tweet is passed to all the publishers.
 * 
 * In the very basic case there are two publishers. The first 
 * one is responsible for saving tweets and the second one
 * reports about the progress back to UI.
 * 
 * This approach allows to create unit tests for every single
 * filter and for every single publisher.
 * 
 */
class TwitterStatusListener implements StatusListener, ConnectionLifeCycleListener{

	private static Logger logger = Logger.getLogger(TwitterStatusListener.class.getName());
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

	private CollectionTask task;

	private List<Predicate<JsonObject>> filters = new ArrayList<>();
	private Collection collection;
	private static int max  = 3;
	private static int min = 1;
	private long timeToSleep = 0;
	private long counter = 0;
	private int threshold = 5;
	private static GenericCache cache;

	private static DataFeedService dataFeedService;
	private static CollectionService collectionService;
	private static EmailService emailService;

	static{
		cache = GenericCache.getInstance();
		dataFeedService = MicroMappersApplication.getApplicationContext().getBean(DataFeedService.class);
		collectionService = MicroMappersApplication.getApplicationContext().getBean(CollectionService.class);
		emailService = MicroMappersApplication.getApplicationContext().getBean(EmailService.class);
	}

	public TwitterStatusListener(CollectionTask task) {
		this.task = task;
		collection = collectionService.getByCode(task.getCollectionCode());
	}

	/**
	 * Adds a filter which is able to ignore some tweets.
	 * 
	 * @param filter
	 *            A function that returns true when document is approved and
	 *            false when document must be ignored.
	 */
	public void addFilter(Predicate<JsonObject> filter) {
		filters.add(filter);
	}

	@Override
	public void onStatus(Status status) {		
		//		task.setSourceOutage(false);
		String json = TwitterObjectFactory.getRawJSON(status);

		JsonObject originalDoc = Json.createReader(new StringReader(json)).readObject();
		if(!(filters.stream().allMatch(f -> f.test(originalDoc)))){
			return;
		}

		/*for (Predicate<JsonObject> filter : filters) {
			if (!filter.test(originalDoc)) {
				//logger.info(originalDoc.get("text").toString() + ": failed on filter = " + filter.getFilterName());
				return;
			}
		}*/

		System.out.println("TweetId: " +originalDoc.getString("id_str"));
		DataFeed dataFeed = new DataFeed();
		dataFeed.setCollection(collection);
		dataFeed.setFeedId(originalDoc.getString("id_str"));
		dataFeed.setProvider(CollectionType.TWITTER);

		//Persisting to dataFeed
		try{
			DataFeed presistedDataFeed = dataFeedService.persistToDbAndFile(dataFeed, originalDoc.toString());

			//incrementing the counterMap
			if(presistedDataFeed != null){
				++counter;
				if (counter >= threshold) {
					cache.incrTwtCounter(collection.getCode(), counter);
					counter = 0;
				}
			}

		}catch(MicromappersServiceException e){
			logger.error("Exception while persisting tweet to db & fileSystem", e);
		}
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		logger.debug(task.getCollectionName() + ": Track limitation notice: " + numberOfLimitedStatuses);
		task.setTwitterStatus(CollectionStatus.RUNNING_WARNING);
		task.setStatusMessage("Track limitation notice: " + numberOfLimitedStatuses);
	}

	@Override
	public void onException(Exception ex) {
		logger.error("Exception for collection " + task.getCollectionCode(), ex);
		int attempt = cache.incrAttempt(task.getCollectionCode());
		task.setStatusMessage(ex.getMessage());
		if(ex instanceof TwitterException)
		{
			if(((TwitterException) ex).getStatusCode() == -1){
				if(attempt > Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_NET_FAILURE_RETRY_ATTEMPTS))){
					task.setTwitterStatus(CollectionStatus.FATAL_ERROR);
				}else{
					timeToSleep = (long) (getRandom()*attempt*
							Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_NET_FAILURE_WAIT_SECONDS)));
					logger.warn("Error -1, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);
					task.setTwitterStatus(CollectionStatus.WARNING);
					task.setStatusMessage("Collection Stopped due to Twitter Error. Reconnect Attempt: " + attempt);
				}
			}
			else if(((TwitterException) ex).getStatusCode() == 420){
				if(attempt > Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_RATE_LIMIT_RETRY_ATTEMPTS))){
					task.setTwitterStatus(CollectionStatus.FATAL_ERROR);
				}else{
					timeToSleep = (long) (getRandom()*(2^(attempt-1))*
							Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_RATE_LIMIT_WAIT_SECONDS)));
					logger.warn("Error 420, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);
					task.setTwitterStatus(CollectionStatus.WARNING);
					task.setStatusMessage("Collection Stopped due to Twitter Error. Reconnect Attempt: " + attempt);
				}
			}
			else if(((TwitterException) ex).getStatusCode() == 503){
				if(attempt > Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_SERVICE_UNAVAILABLE_RETRY_ATTEMPTS))) {
					task.setTwitterStatus(CollectionStatus.FATAL_ERROR);
					//					task.setSourceOutage(true);
				} else {
					timeToSleep = (long) (getRandom()*attempt*
							Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_SERVICE_UNAVAILABLE_WAIT_SECONDS)));
					logger.warn("Error 503, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);
					task.setTwitterStatus(CollectionStatus.WARNING);
					task.setStatusMessage("Collection Stopped due to Twitter Error. Reconnect Attempt: " + attempt);
				}			 
			}
			else{
				task.setTwitterStatus(CollectionStatus.FATAL_ERROR);
			}


			if(task.getTwitterStatus().equals(CollectionStatus.FATAL_ERROR)){
				emailService.sendErrorMail(task.getCollectionCode(),task.getStatusMessage() + "\n" + ex.toString());
			}
			else
			{
				try {
					Thread.sleep(timeToSleep*1000);
				} catch (InterruptedException ignore) {
				}
				timeToSleep=0;
			}
		}
		else if(ex instanceof RejectedExecutionException)
		{
			if(attempt > Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_SERVICE_UNAVAILABLE_RETRY_ATTEMPTS)))
			{
				emailService.sendErrorMail(task.getCollectionCode(),task.getStatusMessage() + "\n" + ex.toString());
				task.setTwitterStatus(CollectionStatus.EXCEPTION);
			}
			else
			{
				timeToSleep = (long) (getRandom()*attempt*
						Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.RECONNECT_SERVICE_UNAVAILABLE_WAIT_SECONDS)));
				logger.warn("Error RejectedExecutionException, Waiting for " + timeToSleep + " seconds, attempt: " + attempt);					
				task.setTwitterStatus(CollectionStatus.WARNING);
				task.setStatusMessage("Collection Stopped due to RejectedExecutionException. Reconnect Attempt: " + attempt);

				try {
					Thread.sleep(timeToSleep*1000);
				} catch (InterruptedException ignore) {
				}
				timeToSleep=0;
			}
		}
		else
		{
			task.setTwitterStatus(CollectionStatus.FATAL_ERROR);
			emailService.sendErrorMail(task.getCollectionCode(),task.getStatusMessage() + "\n" + ex.toString());			
		}
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
	}

	@Override
	public void onStallWarning(StallWarning msg) {
		logger.warn(task.getCollectionCode() + " Stall Warning: " + msg.getMessage());
	}

	private static double getRandom()
	{
		return (Math.random() * (max - min) + min);
	}

	@Override
	public void onConnect() {
		if(task.getTwitterStatus() == CollectionStatus.WARNING)
		{
			task.setStatusMessage("was disconnected due to network failure, reconnected OK");
			cache.resetAttempt(task.getCollectionCode());
		}
		else
			task.setStatusMessage(null);
		task.setTwitterStatus(CollectionStatus.RUNNING);
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onCleanUp() {
		// TODO Auto-generated method stub

	}
}
