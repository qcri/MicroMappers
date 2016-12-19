package org.qcri.micromappers.utility;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

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
//	private JedisPublisher publisherJedis;

	public TwitterStreamTracker(CollectionTask task) throws ParseException{
		logger.info("Waiting to aquire Jedis connection for collection " + task.getCollectionCode());
		this.query = task2query(task);
		Configuration config = task2configuration(task);
//		this.publisherJedis = JedisPublisher.newInstance();
//		logger.info("Jedis connection acquired for collection " + task.getCollectionCode());

//		String channelName = configProperties.getProperty(MicromappersConfigurationProperty.COLLECTOR_CHANNEL) + "." + task.getCollectionCode();
		/*LoadShedder shedder = new LoadShedder(
				Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.PERSISTER_LOAD_LIMIT)),
				Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.PERSISTER_LOAD_CHECK_INTERVAL_MINUTES)), 
				true,channelName);*/
		TwitterStatusListener listener = new TwitterStatusListener(task);
//		listener.addFilter(new ShedderFilter(channelName, shedder));
		if ("strict".equals(task.getGeoR())) {
			listener.addFilter(new StrictLocationFilter(task));
			logger.info("Added StrictLocationFilter for collection = " + task.getCollectionCode() + ", BBox: " + task.getGeoLocation());
		}
		
		// Added by koushik
		if (task.isToFollowAvailable()) {
			listener.addFilter(new FollowFilter(task));
			logger.info("Added FollowFilter for collection = " + task.getCollectionCode() + ", toFollow: " + task.getToFollow());
		}
		
		if (task.isToTrackAvailable() && (task.isGeoLocationAvailable() || task.isToFollowAvailable())) {
			// New default behavior: filter tweets received from geolocation and/or followed users using tracked keywords
			// Note: this override the default and old behavior of ORing the filter conditions by Twitter
			listener.addFilter(new TrackFilter(task));
			logger.info("Added TrackFilter for collection = " + task.getCollectionCode() + ", toTrack: " + task.getToTrack());
		}
//		listener.addPublisher(publisherJedis);
//		long threhold = Long.parseLong(configProperties.getProperty(MicromappersConfigurationProperty.COLLECTOR_REDIS_COUNTER_UPDATE_THRESHOLD));
		String cacheKey = task.getCollectionCode();
//		listener.addPublisher(new StatusPublisher(cacheKey, threhold));

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
//		publisherJedis.close();
		logger.info("AIDR-Fetcher: Collection stopped which was tracking " + query);
	}
	
	private static Configuration task2configuration(CollectionTask task) {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(false)
		        .setJSONStoreEnabled(true)
		        .setOAuthConsumerKey(configProperties.getProperty(MicromappersConfigurationProperty.TWITTER_APP_KEY))
		        .setOAuthConsumerSecret(configProperties.getProperty(MicromappersConfigurationProperty.TWITTER_APP_SECRET))
		        .setOAuthAccessToken(task.getAccessToken())
		        .setOAuthAccessTokenSecret(task.getAccessTokenSecret());
		Configuration configuration = configurationBuilder.build();
		return configuration;
	}
	
	static FilterQuery task2query(CollectionTask collectionTask) throws NumberFormatException {
		FilterQuery query = new FilterQuery();
		
		String toTrack = collectionTask.getToTrack();
		if (StringUtils.isNotBlank(toTrack)){
			query.track(toTrack.split(","));
		}
			
		String toFollow = collectionTask.getToFollow();
		if (StringUtils.isNotBlank(toFollow)) {
			query.follow(Arrays.stream(toFollow.split(",")).mapToLong(Long::parseLong).toArray());
		}

		String locations = collectionTask.getGeoLocation();
		if (StringUtils.isNotBlank(locations)) {
			double[] flat = Arrays.stream(locations.split(",")).mapToDouble(Double::parseDouble).toArray();

			assert flat.length % 4 == 0;
			double[][] square = new double[flat.length / 2][2];
			for (int i = 0; i < flat.length; i = i + 2) {
				// Read 2 elements at a time, into each 2-element sub-array
				// of 'locations'
				square[i / 2][0] = flat[i];
				square[i / 2][1] = flat[i + 1];
			}
			query.locations(square);
		}

		String language = collectionTask.getLanguageFilter();
		if (StringUtils.isNotBlank(language))
			query.language(language.split(","));
		return query;
	}

}
