package org.qcri.micromappers.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.qcri.micromappers.models.CollectionTask;

/**
 *
 * This class is responsible of keeping complex data structures in the main-memory for a fast access. 
 */
public class GenericCache {

	private Map<String, TwitterStreamTracker> twitterTrackerMap = null; //keeps twitter tracker object
	private Map<String, Long> twtCountersMap = null; //keeps downloaded docs counter
	private Map<String, CollectionTask> failedCollections = null; // keeps failed collections
	private Map<String, CollectionTask> twtConfigMap = null; // keeps twitter configuartions tokens and keys of a particular collections
	//    private CollectorStatus collectorStatus; // keeps collector status inforamtion

	private Map<String, CollectionTask> fbConfigMap =  null;
	private Map<String, Long> fbPostCountersMap = null; //keeps downloaded docs counter
	private Map<String, FacebookFeedTracker> fbTrackerMap = null; //keeps facebook tracker object
	private final Map<String, Boolean> fbSyncObjMap;
	private final Map<String, Integer> fbSyncStateMap;
	private final Map<String, Integer> reconnectAttempts;

	private GenericCache() {
		twitterTrackerMap = new HashMap<String, TwitterStreamTracker>();
		twtCountersMap = new ConcurrentHashMap<String, Long>();
		failedCollections = new HashMap<String, CollectionTask>();
		twtConfigMap = new HashMap<String, CollectionTask>();
		fbConfigMap = new HashMap<String, CollectionTask>();
		fbPostCountersMap = new ConcurrentHashMap<String, Long>();
		fbTrackerMap = new HashMap<String, FacebookFeedTracker>();
		//        collectorStatus = new CollectorStatus();
		reconnectAttempts = new HashMap<String,Integer>();
		fbSyncObjMap = new ConcurrentHashMap<String, Boolean>();
		fbSyncStateMap = new ConcurrentHashMap<String, Integer>();
	}

	public static GenericCache getInstance() {
		return GenericSingletonHolder.INSTANCE;
	}

	private static class GenericSingletonHolder {

		private static final GenericCache INSTANCE = new GenericCache();
	}

	/* public void setCollectorStatus(CollectorStatus cStatus){
        this.collectorStatus=cStatus;
    }

    public CollectorStatus getCollectorStatus(){
        return this.collectorStatus;
    }*/

	public void setTwitterTracker(String key, TwitterStreamTracker tracker) {
		this.twitterTrackerMap.put(key, tracker);
	}

	public void delTwitterTracker(String key) {
		this.twitterTrackerMap.remove(key);
	}

	public TwitterStreamTracker getTwitterTracker(String key) {
		return this.twitterTrackerMap.get(key);
	}

	public void setFailedCollection(String key, CollectionTask task) {
		this.failedCollections.put(key, task);
	}

	public void delFailedCollection(String key) {
		this.failedCollections.remove(key);
	}

	public void setFacebookTracker(String key, FacebookFeedTracker tracker) {
		this.fbTrackerMap.put(key, tracker);
	}

	public void delFacebookTracker(String key) {
		this.fbTrackerMap.remove(key);
	}

	public FacebookFeedTracker getFacebookTracker(String key) {
		return this.fbTrackerMap.get(key);
	}

	public CollectionTask getFailedCollectionTask(String key) {

		if (this.failedCollections.containsKey(key)) {
			CollectionTask task = this.failedCollections.get(key).clone();
			return ommitKeys(task);
		}
		return null;
	}

	public List<CollectionTask> getAllFailedCollections() {
		List<CollectionTask> collectionList = new ArrayList<CollectionTask>();
		if (failedCollections != null) {
			for (Map.Entry pairs : failedCollections.entrySet()) {
				CollectionTask oldTask = (CollectionTask) pairs.getValue();
				CollectionTask task = oldTask.clone();
				collectionList.add(ommitKeys(task));
			}

			return collectionList;
		}

		return collectionList;
	}

	public CollectionTask getTwtConfigMap(String key) {
		return twtConfigMap.get(key);
	}

	public void setTwtConfigMap(String key, CollectionTask config) {
		this.twtConfigMap.put(key, config);
	}

	public void delTwtConfigMap(String key) {
		this.twtConfigMap.remove(key);
	}

	public CollectionTask getFbConfigMap(String key) {
		return fbConfigMap.get(key);
	}

	public void setFbConfigMap(String key, CollectionTask config) {
		this.fbConfigMap.put(key, config);
	}

	public void delFbConfigMap(String key) {
		this.fbConfigMap.remove(key);
	}

	public List<CollectionTask> getAllRunningCollectionTasks(){
		List<CollectionTask> collections = new ArrayList<CollectionTask>();

		if (twtConfigMap != null) {
			for (Map.Entry pairs : twtConfigMap.entrySet()) {
				CollectionTask oldTask = (CollectionTask) pairs.getValue();
				CollectionTask task = oldTask.clone();
				Long tweetsCounter = this.twtCountersMap.get(task.getCollectionCode());
				task.setTweetCount(tweetsCounter);
				collections.add(task);
			}
		}
		if (fbConfigMap != null) {
			for (Map.Entry pairs : fbConfigMap.entrySet()) {
				CollectionTask oldTask = (CollectionTask) pairs.getValue();
				CollectionTask task = oldTask.clone();
				Long fbPostCounter = this.fbPostCountersMap.get(task.getCollectionCode());
				task.setFbPostCount(fbPostCounter);
				collections.add(task);
			}
		}
		return collections;
	}

	/*public List<CollectionTask> getAllConfigs() {
		List<CollectionTask> mappersList = new ArrayList<CollectionTask>();
		if (twtConfigMap != null) {
			for (Map.Entry pairs : twtConfigMap.entrySet()) {
				CollectionTask oldTask = (CollectionTask) pairs.getValue();
				CollectionTask task = oldTask.clone();
				Long tweetsCounter = this.twtCountersMap.get(task.getCollectionCode());
				if(tweetsCounter == null)
					tweetsCounter = 0L;
				task.setTweetCount(tweetsCounter);
				mappersList.add(ommitKeys(task));
			}
		}
		  if (fbConfigMap != null) {
            for (Map.Entry pairs : fbConfigMap.entrySet()) {
                CollectionTask oldTask = (CollectionTask) pairs.getValue();
                CollectionTask task = oldTask.clone();
                Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
                if(tweetsCounter == null)
                    tweetsCounter = 0L;
                String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
                task.setCollectionCount(tweetsCounter);
                task.setLastDocument(lastDownloadedDoc);
                mappersList.add(ommitKeys(task));
            }
        }
		return mappersList;
	}*/

	public CollectionTask ommitKeys(CollectionTask task) {

		task.setAccessToken(null);
		task.setAccessTokenSecret(null);
		return task;
	}

	public CollectionTask getTwitterConfig(String key) {

		if (this.twtConfigMap.containsKey(key)) {
			CollectionTask task = this.twtConfigMap.get(key).clone();
			if (task != null) {
				Long tweetsCounter = this.twtCountersMap.get(task.getCollectionCode());
				if(tweetsCounter == null)
					tweetsCounter = 0L;
				task.setTweetCount(tweetsCounter);
			}
			return ommitKeys(task);
		}
		return null;
	}

	public CollectionTask getFacebookConfig(String key) {

		if (this.fbConfigMap.containsKey(key)) {
			CollectionTask task = this.fbConfigMap.get(key).clone();
			if (task != null) {
				Long fbPostCounter = this.fbPostCountersMap.get(task.getCollectionCode());
				if(fbPostCounter == null)
					fbPostCounter = 0L;
				task.setFbPostCount(fbPostCounter);
			}return ommitKeys(task);
		}
		return null;
	}

	public boolean isRuningTwtConfigExists(CollectionTask qm) {
		for (Map.Entry pairs : twtConfigMap.entrySet()) {
			CollectionTask storedQM = (CollectionTask) pairs.getValue();
			if (storedQM != null) {
				if (storedQM.equals(qm)) {
					if (qm.getTwitterStatus() != null) {
						if (!(qm.getTwitterStatus().equals(CollectionStatus.FATAL_ERROR.toString()))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isConfigExists(CollectionTask task) {

		// check for twitter collection
		for (Map.Entry pairs : twtConfigMap.entrySet()) {
			CollectionTask storedTask = (CollectionTask) pairs.getValue();
			if (storedTask.equals(task)) {
				return true;
			}
		}

		// check for fb collection
		for (Map.Entry pairs : fbConfigMap.entrySet()) {
			CollectionTask storedTask = (CollectionTask) pairs.getValue();
			if (storedTask.equals(task)) {
				return true;
			}
		}
		return false;
	}

	// keeps track of tweets counters for various running collections
	public void incrTwtCounter(String key, Long counter) {
		if (twtCountersMap.containsKey(key)) {
			twtCountersMap.put(key, twtCountersMap.get(key) + counter);
		} else {
			twtCountersMap.put(key, counter);
		}
	}

	// keeps track of fb posts counters for various running collections
	public void incrFbCounter(String key, Long counter) {
		if (fbPostCountersMap.containsKey(key)) {
			fbPostCountersMap.put(key, fbPostCountersMap.get(key) + counter);
		} else {
			fbPostCountersMap.put(key, counter);
		}
	}

	public void deleteTwtCounter(String key) {
		twtCountersMap.remove(key);
	}

	public void deleteFbPostCounter(String key) {
		fbPostCountersMap.remove(key);
	}

	public List<TwitterStreamTracker> getAllTwitterTrackers() {
		List<TwitterStreamTracker> trackersList = new ArrayList<TwitterStreamTracker>();
		for (Map.Entry pairs : twitterTrackerMap.entrySet()) {
			String key = (String) pairs.getKey();
			trackersList.add((TwitterStreamTracker) pairs.getValue());
		}

		return trackersList;
	}

	//keep track to number of reconnect attempts for for various running collections
	public int incrAttempt(String key) {
		if (reconnectAttempts.containsKey(key)) {
			reconnectAttempts.put(key, reconnectAttempts.get(key) + 1);
		} else {
			reconnectAttempts.put(key, 1);
		}
		return reconnectAttempts.get(key);
	}

	//reset number of reconnect attempts when collection is able to establish connection
	public void resetAttempt(String key) {
		if (reconnectAttempts.containsKey(key)) {
			reconnectAttempts.put(key, 0);
		}
	}

	public int getReconnectAttempts(String key) {
		return reconnectAttempts.get(key);
	}

	public void delReconnectAttempts(String key) {
		reconnectAttempts.remove(key);
	}

	public List<CollectionTask> getEligibleFacebookCollectionsToRun() {

		List<CollectionTask> collectionTaskList = fbConfigMap.entrySet().stream().map(pair->pair.getValue()).filter(task -> {
			if(task.getLastExecutionTime() != null && (task.getLastExecutionTime().getTime() + task.getFetchInterval()) >= new Date().getTime()) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());

		return collectionTaskList;
	}

	public Boolean getFbSyncObjMap(String key){
		if(fbSyncObjMap.containsKey(key)){
			return fbSyncObjMap.get(key);
		}
		return null;
	}

	public void setFbSyncObjMap(String key, Boolean obj){
		fbSyncObjMap.put(key, obj);
	}

	public void delFbSyncObjMap(String key){
		fbSyncObjMap.remove(key);
	}
	
	public Integer getFbSyncStateMap(String key){
    	if(fbSyncStateMap.containsKey(key)){
    		return fbSyncStateMap.get(key);
    	}
    	return null;
    }
    
    public void setFbSyncStateMap(String key, Integer state){
    	fbSyncStateMap.put(key, state);
    	
    }
    
    public void delFbSyncStateMap(String key){
    	fbSyncStateMap.remove(key);
    }

}
