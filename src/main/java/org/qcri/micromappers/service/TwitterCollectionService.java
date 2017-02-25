
package org.qcri.micromappers.service;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.CollectionLabel;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.UserConnection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.models.TwitterProfile;
import org.qcri.micromappers.repository.CollectionRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.GenericCache;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.qcri.micromappers.utility.twitterCrawler.TweetManager;
import org.qcri.micromappers.utility.twitterCrawler.TwitterCriteria;
import org.springframework.stereotype.Service;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterCollectionService
{	
	@Inject
	private CollectionRepository collectionRepository;
	@Inject
	private UserConnectionService userConnectionService;
	@Inject
	private TweetManager tweetManager;
	@Inject
	private DataFeedService dataFeedService;
	@Inject
	private CollectionLogService collectionLogService;
	@Inject
	private Util util;

	private static Logger logger = Logger.getLogger(TwitterCollectionService.class);

	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

	public Collection getByAccountAndGlideMaster(Account account, GlideMaster glideMaster)
	{
		try{
			return collectionRepository.getByAccountAndGlideMaster(account, glideMaster);
		}catch (Exception e) {
			logger.error("Error while fetching collection by Account and GlideMaster", e);
			throw new MicromappersServiceException("Error while fetching collection by Account and GlideMaster", e);
		}
	}

	public List<TwitterProfile> searchUsers(String keyword, Integer limit){
		List<TwitterProfile> searchedUsersList = new ArrayList<TwitterProfile>();
		Integer tempLimit = limit;

		Account authenticatedUser = util.getAuthenticatedUser();
		if(authenticatedUser != null){
			UserConnection userConnection = userConnectionService.getByProviderIdAndUserId(CollectionType.TWITTER.getValue(), authenticatedUser.getUserName());
			if(userConnection != null){
				Configuration twitterConfiguration = getTwitterConfiguration(userConnection.getAccessToken(), userConnection.getSecret());
				Twitter twitter = new TwitterFactory(twitterConfiguration).getInstance();

				int pageNumber = 1;
				try {
					do{
						ResponseList<User> users = twitter.searchUsers(keyword, pageNumber);
						//String rawJSON = TwitterObjectFactory.getRawJSON(users);

						if(users != null && users.size() == 20){
							tempLimit -= users.size();
							pageNumber++;

						}else{
							tempLimit = -1;
						}
						if(users != null){
							searchedUsersList.addAll(users.stream().map(u -> toTwitterProfile(u)).collect(Collectors.toList()));
						}
					}while(tempLimit > 0);
				} catch (TwitterException e) {
					logger.error("Exception while searching users list.", e);
				}
			}else{
				logger.warn("User had not connected twitter connection.");
			}
		}else{
			logger.warn("User is not authenticated.");
		}

		if(searchedUsersList.size() > limit){
			return searchedUsersList.subList(0, limit);
		}else{
			return searchedUsersList;
		}
	}


	/** This method is used to crawl the older tweets by criteria.
	 * @param collectionTask
	 */
	public void crawlTweets(CollectionTask collectionTask){

		
		
		DateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_FORMAT);
		if(collectionTask != null){
			if(collectionTask.getTwitterUntilDate() != null){
				TwitterCriteria criteria = TwitterCriteria.create();

				criteria.setUntil(dateFormatter.format(collectionTask.getLastExecutionTime() != null ? collectionTask.getLastExecutionTime() : collectionTask.getTwitterUntilDate()));

				if(collectionTask.getTwitterSinceDate() != null){
					criteria.setSince(dateFormatter.format(collectionTask.getTwitterSinceDate()));
				}

				if(StringUtils.isNotBlank(collectionTask.getToTrack())){
					criteria.setQuerySearch(collectionTask.getToTrack());
				}

				logger.info("Starting the twitter crawler for collection Code: "+collectionTask.getCollectionCode() + "with query : "+ criteria.toString());
				tweetManager.getTweets(collectionTask, criteria);
			}
		}
	}


	/**
	 * This method is used to search the full tweets from twitterAPI by tweetIds.
	 * 
	 * @param tweetIds
	 * @param accessToken
	 * @param accessTokenSecret
	 * @return
	 * @throws TwitterException
	 */
	public List<JsonObject> searchTweets(List<Long> tweetIds, String accessToken, String accessTokenSecret) throws TwitterException{
		List<JsonObject> searchedStatusesList = new ArrayList<JsonObject>();
		Configuration twitterConfiguration = getTwitterConfiguration(accessToken, accessTokenSecret);
		Twitter twitter = new TwitterFactory(twitterConfiguration).getInstance();
		int start = 0, end = Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT;
		try {

			do{
				long[] tweetIdsArray = null;

				tweetIdsArray = tweetIds.subList(start, Math.min(end, tweetIds.size())).stream().mapToLong(Long::longValue).toArray();

				try{
					ResponseList<Status> statuses = twitter.lookup(tweetIdsArray);
					String rawJSON = TwitterObjectFactory.getRawJSON(statuses);
					JsonArray fullTweetsJsonArray = Json.createReader(new StringReader(rawJSON)).readArray();

					for (int i = 0; i < fullTweetsJsonArray.size(); i++) {
						searchedStatusesList.add(fullTweetsJsonArray.getJsonObject(i));
					}
					start = end;
					end += Constants.MAX_TWEET_LOOKUP_COUNT_IN_ONE_HIT;

				}catch(TwitterException e ){
					logger.error("Exception while searching tweets by ids.", e);
					throw e;
				}
			}while(end < tweetIds.size());

		}catch(Exception e2){
			logger.error("Exception while searching tweets by ids.", e2);
		}
		return searchedStatusesList;
	}


	/** This method is used to persist the tweets in db and fileSystem.
	 * @param collection
	 * @param tweets
	 */
	public void persistTweets(Collection collection, List<JsonObject> tweets, CollectionLabel collectionLabel){
		DataFeed dataFeed = null;
		long counter = 0;
		for (JsonObject tweet : tweets) {
			dataFeed = new DataFeed();
			dataFeed.setCollection(collection);
			dataFeed.setFeedId(tweet.getString("id_str"));
			dataFeed.setProvider(CollectionType.TWITTER);

			//Persisting to dataFeed
			try{
				DataFeed presistedDataFeed = dataFeedService.persistToDbAndFile(dataFeed, tweet, collectionLabel != null);

				if(presistedDataFeed != null){
					counter++;
				}

			}catch(MicromappersServiceException e){
				logger.error("Exception while persisting tweet to db & fileSystem", e);
			}
		}

		//Updating collection count
		if(GenericCache.getInstance().getTwitterConfig(collection.getCode()) != null){
			GenericCache.getInstance().incrTwtCounter(collection.getCode(), counter);
		}else{
			collectionLogService.incrementCount(collection.getId(), counter, CollectionType.TWITTER);
		}
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

	private TwitterProfile toTwitterProfile(User user){
		TwitterProfile twitterProfile = new TwitterProfile();
		twitterProfile.setId(String.valueOf(user.getId()));
		twitterProfile.setName(user.getName());
		twitterProfile.setScreenName(user.getScreenName());
		twitterProfile.setFollowersCount(user.getFollowersCount());
		twitterProfile.setFriendsCount(user.getFriendsCount());
		twitterProfile.setImageURL(user.getProfileImageURL());
		return twitterProfile;
	}
}
