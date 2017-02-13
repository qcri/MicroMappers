package org.qcri.micromappers.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Account;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.UserConnection;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.TwitterProfile;
import org.qcri.micromappers.repository.CollectionRepository;
import org.qcri.micromappers.utility.CollectionType;
import org.qcri.micromappers.utility.Util;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.stereotype.Service;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
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
