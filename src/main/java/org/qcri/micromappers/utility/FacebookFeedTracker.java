package org.qcri.micromappers.utility;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.qcri.micromappers.MicroMappersApplication;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.exception.MicromappersServiceException;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.models.FacebookProfile;
import org.qcri.micromappers.service.CollectionService;
import org.qcri.micromappers.service.DataFeedService;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Ordering;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.internal.logging.Logger;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

public class FacebookFeedTracker implements Closeable {

	private static Logger logger = Logger.getLogger(FacebookFeedTracker.class);
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
	private Facebook facebook;
	private final CollectionTask task;
	private final LoadShedder fbApiHitShedder;
	private Collection collection;
	
	private static DataFeedService dataFeedService;
	private static CollectionService collectionService;
	
	private static final int DEFAULT_LIMIT = 100;
	private static final Long HOUR_IN_MILLISECS = 60 * 60 * 1000L;
	
	static{
		dataFeedService = MicroMappersApplication.getApplicationContext().getBean(DataFeedService.class);
		collectionService = MicroMappersApplication.getApplicationContext().getBean(CollectionService.class);
	}
	private static String FIELDS_TO_FETCH = "id,updated_time,message_tags,scheduled_publish_time,"
			+ "created_time, full_picture,object_id,with_tags, is_published, "
			+ "from,to,message,picture,link,name,caption,description,source,properties,"
			+ "icon,actions,privacy,type,shares,status_type,place,story,"
			+ "application,targeting,likes.summary(true),comments.summary(true)";

	public FacebookFeedTracker(String accessToken){
		task = null;
		fbApiHitShedder = null;
		this.facebook = getFacebookInstance(accessToken);
	}
	
	public FacebookFeedTracker(CollectionTask task) {
		this.facebook = getFacebookInstance(task.getAccessToken());
		fbApiHitShedder = new LoadShedder(Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.FACEBOOK_MAX_API_HITS_HOURLY_PER_USER)),
				Integer.parseInt(configProperties.getProperty(MicromappersConfigurationProperty.FACEBOOK_LOAD_CHECK_INTERVAL_MINUTES)),
				true, "FACEBOOK_API_CALL_SHEDDER." + task.getCollectionCode());
		this.task = task;
		collection = collectionService.getByCode(task.getCollectionCode());
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Boolean syncObj = GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode()) == null ? Boolean.TRUE : GenericCache.getInstance().getFbSyncObjMap(task.getCollectionCode());
				synchronized (syncObj) {
					GenericCache.getInstance().setFbSyncObjMap(task.getCollectionCode(), syncObj);
					collectFacebookData();
				}

			}
		}).start();
	}

	@Override
	public void close() throws IOException {
		facebook.shutdown();
		logger.info("AIDR-Fetcher: Collection stopped which was tracking ");
	}

	private static Facebook getFacebookInstance(String accessToken) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setDebugEnabled(false)
				.setOAuthAppId(configProperties.getProperty(MicromappersConfigurationProperty.FACEBOOK_APP_KEY))
				.setOAuthAppSecret(
						configProperties.getProperty(MicromappersConfigurationProperty.FACEBOOK_APP_SECRET))
				.setJSONStoreEnabled(true).setOAuthAccessToken(accessToken);

		Configuration configuration = builder.build();
		Facebook instance = new FacebookFactory(configuration).getInstance();
		return instance;
		
	}

	public void collectFacebookData() {

		Date toTimestamp = new Date();
		long fetchFromInMiliSecs = task.getFetchFrom() * HOUR_IN_MILLISECS;
		Date fromTimestamp = new Date(System.currentTimeMillis() - fetchFromInMiliSecs);
		
		try {
			//Search all profiles by keywords
			List<FacebookProfile> fbProfiles = new ArrayList<FacebookProfile>();
			
			//Will uncomment the below code if we have to search posts by keywords also
			/*if(StringUtils.isNotBlank(task.getToTrack())){
				fbProfiles = searchProfiles(task.getToTrack(), -1, 0);
			}*/
			
			//Add subscribed profilesIds to list
			if(StringUtils.isNotBlank(task.getSubscribedProfiles())){
				try {
					ObjectMapper mapper = new ObjectMapper();
					List<FacebookProfile> subscribedProfiles = mapper.readValue(task.getSubscribedProfiles(), mapper.getTypeFactory().constructCollectionType(List.class, FacebookProfile.class));
					fbProfiles.addAll(subscribedProfiles);
				} catch (IOException e) {
					logger.error("Exception while parsing facebook subscribed page json",e);
				}
			}

			if(task.getLastExecutionTime() != null && 
					(System.currentTimeMillis() - task.getLastExecutionTime().getTime()) <= fetchFromInMiliSecs) {
				fromTimestamp = task.getLastExecutionTime();
			}
			
			this.processPost(toTimestamp, fromTimestamp, fbProfiles);
			
			task.setLastExecutionTime(toTimestamp);
			GenericCache.getInstance().setFbConfigMap(task.getCollectionCode(), task);
		} catch (FacebookException e) {
			GenericCache.getInstance().setFailedCollection(task.getCollectionCode(), task);
		}
	}

	private void processPost(Date toTimestamp, Date since, List<FacebookProfile> fbProfiles)
			throws FacebookException {

		Set<String> processedFbProfileIdsSet = new HashSet<String>();
		for (FacebookProfile fbProfile : fbProfiles) {
			
			if(processedFbProfileIdsSet.contains(fbProfile.getId())){
				continue;
			}else{
				processedFbProfileIdsSet.add(fbProfile.getId());
			}
			
			int postsOffset = 0;
			while (postsOffset >= 0 ) {

				while(!fbApiHitShedder.canProcess())
				{
					try {
						long deltaMillis = (System.currentTimeMillis() - fbApiHitShedder.getLastSetTime());
						Thread.sleep(deltaMillis);
					} catch (InterruptedException e) {
						logger.warn("Interrupted exception while sleeping in load shedder for collection code: "
								+ task.getCollectionCode());
					}
				}
				try {
					ResponseList<Post> feed = facebook.getFeed(fbProfile.getId(), new Reading().fields(FIELDS_TO_FETCH)
							.since(since).until(toTimestamp).order(Ordering.CHRONOLOGICAL).limit(DEFAULT_LIMIT)
							.offset(postsOffset));
					postsOffset = feed.size() == DEFAULT_LIMIT ? postsOffset + DEFAULT_LIMIT : -1;
					Long count = 0L;
					for (Post post : feed) {
//						System.out.println("PostId: " +post.getId());
						DataFeed dataFeed = new DataFeed();
						dataFeed.setCollection(collection);
						dataFeed.setFeedId(post.getId());
						dataFeed.setProvider(CollectionType.FACEBOOK);
						
						//Persisting to dataFeed
						try{
							String postJson = new ObjectMapper().writeValueAsString(post);
							DataFeed persistedDataFeed = dataFeedService.persistToDbAndFile(dataFeed, postJson);
							if(persistedDataFeed != null){
								count++;
							}
						}catch(MicromappersServiceException | JsonProcessingException e){
							logger.error("Exception while persisting tweet to db & fileSystem", e);
						}
					}

					GenericCache.getInstance().incrFbCounter(task.getCollectionCode(), count);
					count = 0L;
				} catch (FacebookException e) {
					logger.warn("Exception while fetching feeds for id: " + fbProfile.getId());
					handleFacebookException(e, task.getCollectionCode());
					postsOffset = -1;
				}
			}
		}
	}

	
	public List<FacebookProfile>  searchProfiles(String keyword, Integer limit, Integer offset){
		List<FacebookProfile> searchedProfiles = new ArrayList<FacebookProfile>();
		if(limit != -1){
			searchedProfiles.addAll(fetchPages(keyword, limit, offset));
			searchedProfiles.addAll(fetchGroups(keyword, limit, offset));
			searchedProfiles.addAll(fetchEvents(keyword, limit, offset));
		}
		else{
			//To fetch all profiles
			limit = DEFAULT_LIMIT;
			while(offset >= 0){
				List<FacebookProfile> profiles = fetchPages(keyword, limit, offset);
				searchedProfiles.addAll(profiles);
				offset = profiles.size() == limit ? offset + limit : -1;
			}
			offset = 0;
			while(offset >= 0){
				List<FacebookProfile> profiles = fetchGroups(keyword, limit, offset);
				searchedProfiles.addAll(profiles);
				offset = profiles.size() == limit ? offset + limit : -1;
			}
			offset = 0;
			while(offset >= 0){
				List<FacebookProfile> profiles = fetchEvents(keyword, limit, offset);
				searchedProfiles.addAll(profiles);
				offset = profiles.size() == limit ? offset + limit : -1;
			}
		}
    	return searchedProfiles;
	}
	
	public List<FacebookProfile> fetchPages(String keyword, Integer limit, Integer offset) {
		if(limit>DEFAULT_LIMIT || limit < 0){
			limit = DEFAULT_LIMIT;
		}
		
		List<FacebookProfile> fbProfiles = new ArrayList<FacebookProfile>();
		
		if (offset >= 0) {
			ResponseList<JSONObject> pageList = null;
			try {
				pageList = facebook.search(keyword,"page", 
						new Reading().fields("id,name,link,likes.summary(true),fan_count,picture").limit(limit).offset(offset));
			} catch (FacebookException e) {
				logger.error("Exception while searching Facebook pages for keyword: "+keyword, e);
			}
			if(CollectionUtils.isNotEmpty(pageList)){
				FacebookProfile facebookProfile = null;
				for (JSONObject jsonObject : pageList) {
					try{
						facebookProfile = new FacebookProfile();
						facebookProfile.setId(jsonObject.getString("id"));
						facebookProfile.setLink(jsonObject.getString("link"));
						facebookProfile.setName(jsonObject.getString("name"));
						facebookProfile.setFans(jsonObject.getInt("fan_count"));
						facebookProfile.setType(FacebookEntityType.PAGE);
						facebookProfile.setImageUrl((jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")));
						fbProfiles.add(facebookProfile);
					}catch(JSONException e){
						logger.warn("Exception while parsing page Json", e.getMessage());
					}
				}
			}
		}
		
		return fbProfiles;
	}


	public List<FacebookProfile> fetchGroups(String keyword, Integer limit, Integer offset) {
		if(limit>DEFAULT_LIMIT || limit < 0){
			limit = DEFAULT_LIMIT;
		}
		
		List<FacebookProfile> fbProfiles = new ArrayList<FacebookProfile>();
		
		if (offset >= 0) {
			ResponseList<JSONObject> groupList = null;
			try {
				groupList = facebook.search(keyword,"group",
						new Reading().fields("id,name,link,picture").limit(limit).offset(offset));
			} catch (FacebookException e) {
				logger.error("Exception while searching Facebook groups for keyword: "+keyword, e);
			}
			if(CollectionUtils.isNotEmpty(groupList)){
				FacebookProfile facebookProfile = null;
				for (JSONObject jsonObject : groupList) {
					try{
						facebookProfile = new FacebookProfile();
						facebookProfile.setId(jsonObject.getString("id"));
						facebookProfile.setLink("https://www.facebook.com/groups/"+facebookProfile.getId());
						facebookProfile.setName(jsonObject.getString("name"));
						facebookProfile.setImageUrl(jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
						facebookProfile.setType(FacebookEntityType.GROUP);
						fbProfiles.add(facebookProfile);
					}catch(JSONException e){
						logger.warn("Exception while parsing group Json", e.getMessage());
					}
				}
			}
			
		}
		
		return fbProfiles;
	}
	
	
	public List<FacebookProfile> fetchEvents(String keyword, Integer limit, Integer offset) {
		if(limit>DEFAULT_LIMIT || limit < 0){
			limit = DEFAULT_LIMIT;
		}
		
		List<FacebookProfile> fbProfiles = new ArrayList<FacebookProfile>();
		
		if (offset >= 0) {
			ResponseList<JSONObject> eventList = null;
			try {
				eventList = facebook.search(keyword,"event", 
						new Reading().fields("id,name,link,picture").limit(limit).offset(offset));
			} catch (FacebookException e) {
				logger.error("Exception while searching Facebook events for keyword: "+keyword, e);
			}
			if(CollectionUtils.isNotEmpty(eventList)){
				FacebookProfile facebookProfile = null;
				for (JSONObject jsonObject : eventList) {
					try{
						facebookProfile = new FacebookProfile();
						facebookProfile.setId(jsonObject.getString("id"));
						facebookProfile.setLink("https://www.facebook.com/events/"+facebookProfile.getId());
						facebookProfile.setName(jsonObject.getString("name"));
						facebookProfile.setImageUrl(jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));
						facebookProfile.setType(FacebookEntityType.EVENT);
						fbProfiles.add(facebookProfile);
					}catch(JSONException e){
						logger.warn("Exception while parsing event Json", e.getMessage());
					}
				}
			}
		}
		return fbProfiles;
	}

	
	private void handleFacebookException(FacebookException e, String collectionCode) throws FacebookException {
		boolean found = false;
		switch (e.getErrorCode()) {
		case 1:
		case 2:
		case 4:
		case 17:
		case 341:
			logger.error("Facebook api is rate limited for collectionCode: " + collectionCode, e);
			found = true;
			break;

		case 102:
			logger.error("Oauth Exception. May be access token got expired for collectionCode: " + collectionCode, e);
			throw new FacebookException(e);
		}

		if (!found) {
			switch (e.getErrorSubcode()) {
			case 458:
			case 459:
			case 460:
			case 463:
			case 464:
			case 467:
				logger.error("Oauth Exception. May be access token got expired for collectionCode: " + collectionCode,
						e);
				throw new FacebookException(e);
			default:
				logger.error("Facebook Exception", e);
			}
		}
	}
}
