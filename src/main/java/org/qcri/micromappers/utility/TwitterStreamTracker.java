package org.qcri.micromappers.utility;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class is responsible for managing all the resources associated with the
 * thread which pulls tweets for the given collection task.
 * 
 */
public class TwitterStreamTracker implements Closeable {

	private static Logger logger = Logger.getLogger(TwitterStreamTracker.class.getName());

	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

	private TwitterStream twitterStream;
	private FilterQuery query;

	public TwitterStreamTracker(CollectionTask task) {
		this.query = task2query(task);
		Configuration config = task2configuration(task);
		TwitterStatusListener listener = new TwitterStatusListener(task);

		/*if (task.isToTrackAvailable()) {
			// New default behavior: filter tweets received from geolocation and/or followed users using tracked keywords
			// Note: this override the default and old behavior of ORing the filter conditions by Twitter
			listener.addFilter(new TrackFilter(task));
			logger.info("Added TrackFilter for collection = " + task.getCollectionCode() + ", toTrack: " + task.getToTrack());
		}*/

		twitterStream = new TwitterStreamFactory(config).getInstance();
		twitterStream.addListener(listener);
		twitterStream.addConnectionLifeCycleListener(listener);
	}

	/**
	 * This method internally creates a thread which manipulates TwitterStream
	 * and calls these adequate listener methods continuously
	 */
	public void start() {
		twitterStream.filter(query);
	}

	public void close() throws IOException {
		twitterStream.cleanUp();
		twitterStream.shutdown();
		logger.info("Collection stopped which was tracking " + query);
	}

	private static Configuration task2configuration(CollectionTask task) {
		return getTwitterConfiguration(task.getAccessToken(), task.getAccessTokenSecret());
	}
	
	private static Configuration getTwitterConfiguration(String accessToken, String accessTokenSecret) {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(false)
		.setJSONStoreEnabled(true)
		.setOAuthConsumerKey(configProperties.getProperty(MicromappersConfigurationProperty.TWITTER_APP_KEY))
		.setOAuthConsumerSecret(configProperties.getProperty(MicromappersConfigurationProperty.TWITTER_APP_SECRET))
		.setOAuthAccessToken(accessToken)
		.setOAuthAccessTokenSecret(accessTokenSecret);
		Configuration configuration = configurationBuilder.build();
		return configuration;
	}
	
	static FilterQuery task2query(CollectionTask collectionTask) throws NumberFormatException {
		FilterQuery query = new FilterQuery();

		String toTrack = collectionTask.getToTrack();
		if (StringUtils.isNotBlank(toTrack)){
			query.track(toTrack.split(","));
		}

		String language = collectionTask.getLanguageFilter();
		if (StringUtils.isNotBlank(language))
			query.language(language.split(","));
		return query;
	}

}
