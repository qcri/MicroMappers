package org.qcri.micromappers.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.qcri.micromappers.models.CollectionTask;

/**
 *
 * This class is responsible of keeping complex data structures in the main-memory for a fast access. 
 */
public class GenericCache {

	private Map<String, TwitterStreamTracker> twitterTrackerMap = null; //keeps twitter tracker object
	private Map<String, Long> countersMap = null; //keeps downloaded docs counter
	private Map<String, CollectionTask> failedCollections = null; // keeps failed collections
	private Map<String, CollectionTask> twtConfigMap = null; // keeps twitter configuartions tokens and keys of a particular collections
	//    private CollectorStatus collectorStatus; // keeps collector status inforamtion
	//    private Map<String, FacebookCollectionTask> fbConfigMap =  null;
	//    private Map<String, FacebookFeedTracker> fbTrackerMap = null; //keeps twitter tracker object
	private final Map<String, Integer> reconnectAttempts;
	//    private final Map<String, Boolean> fbSyncObjMap;
	//    private final Map<String, Integer> fbSyncStateMap;

	private GenericCache() {
		twitterTrackerMap = new HashMap<String, TwitterStreamTracker>();
		countersMap = new ConcurrentHashMap<String, Long>();
		failedCollections = new HashMap<String, CollectionTask>();
		twtConfigMap = new HashMap<String, CollectionTask>();
		//        fbConfigMap = new HashMap<String, FacebookCollectionTask>();
		//        fbTrackerMap = new HashMap<String, FacebookFeedTracker>();
		//        collectorStatus = new CollectorStatus();
		reconnectAttempts = new HashMap<String,Integer>();
		//        fbSyncObjMap = new ConcurrentHashMap<String, Boolean>();
		//        fbSyncStateMap = new ConcurrentHashMap<String, Integer>();
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

	/* public void setFacebookTracker(String key, FacebookFeedTracker tracker) {
        this.fbTrackerMap.put(key, tracker);
    }

    public void delFacebookTracker(String key) {
        this.fbTrackerMap.remove(key);
    }

    public FacebookFeedTracker getFacebookTracker(String key) {
        return this.fbTrackerMap.get(key);
    }*/

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

	/* public FacebookCollectionTask getFbConfigMap(String key) {
        return fbConfigMap.get(key);
    }*/

	/* public void setFbConfigMap(String key, FacebookCollectionTask config) {
        this.fbConfigMap.put(key, config);
    }

    public void delTwtConfigMap(String key) {
        this.twtConfigMap.remove(key);
    }

    public void delFbConfigMap(String key) {
        this.fbConfigMap.remove(key);
    }*/


	public List<CollectionTask> getAllRunningCollectionTasks(){
		List<CollectionTask> collections = new ArrayList<CollectionTask>();

		if (twtConfigMap != null) {
			for (Map.Entry pairs : twtConfigMap.entrySet()) {
				CollectionTask oldTask = (CollectionTask) pairs.getValue();
				CollectionTask task = oldTask.clone();
				Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
				task.setCollectionCount(tweetsCounter);
				collections.add(task);
			}
		}
		/* if (fbConfigMap != null) {
             for (Map.Entry pairs : fbConfigMap.entrySet()) {
                 CollectionTask oldTask = (CollectionTask) pairs.getValue();
                 CollectionTask task = oldTask.clone();
                 Long fbPostCounter = this.countersMap.get(task.getCollectionCode());
                 String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
                 task.setCollectionCount(fbPostCounter);
                 task.setLastDocument(lastDownloadedDoc);
                 collections.add(task);
             }
         }*/
		return collections;
	}

	public Boolean isCollectionRunning(String collectionCode){
		if (twtConfigMap != null) {
			if(twtConfigMap.containsKey(collectionCode)){
				return true;
			}
		}
		return false;
	}


	public List<CollectionTask> getAllConfigs() {
		List<CollectionTask> mappersList = new ArrayList<CollectionTask>();
		if (twtConfigMap != null) {
			for (Map.Entry pairs : twtConfigMap.entrySet()) {
				CollectionTask oldTask = (CollectionTask) pairs.getValue();
				CollectionTask task = oldTask.clone();
				Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
				if(tweetsCounter == null)
					tweetsCounter = 0L;
				task.setCollectionCount(tweetsCounter);
				mappersList.add(ommitKeys(task));
			}
		}
		/*  if (fbConfigMap != null) {
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
        }*/
		return mappersList;
	}

	public CollectionTask ommitKeys(CollectionTask task) {

		task.setAccessToken(null);
		task.setAccessTokenSecret(null);
		return task;
	}

	public CollectionTask getTwitterConfig(String key) {

		if (this.twtConfigMap.containsKey(key)) {
			CollectionTask task = this.twtConfigMap.get(key).clone();
			if (task != null) {
				Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
				if(tweetsCounter == null)
					tweetsCounter = 0L;
				task.setCollectionCount(tweetsCounter);
			}
			return ommitKeys(task);
		}
		return null;
	}

	/* public FacebookCollectionTask getFacebookConfig(String id) {

    	FacebookCollectionTask task =  null;
        if (!(this.fbConfigMap.containsKey(id))) {
            return null;
        }

    	task = this.fbConfigMap.get(id).clone();

        if (task != null) {
            Long tweetsCounter = this.countersMap.get(task.getCollectionCode());
            if(tweetsCounter == null)
                tweetsCounter = 0L;
            String lastDownloadedDoc = this.lastDownloadedDocumentMap.get(task.getCollectionCode());
            task.setCollectionCount(tweetsCounter);
            task.setLastDocument(lastDownloadedDoc);
            task.setAccessToken(null);
            task.setAccessTokenSecret(null);
        } else {
            task = (FacebookCollectionTask) this.failedCollections.get(id);
            if (task != null) {
                task.setAccessToken(null);
                task.setAccessTokenSecret(null);
            }
        }

        return task;

    }*/

	public boolean isRuningTwtConfigExists(CollectionTask qm) {
		for (Map.Entry pairs : twtConfigMap.entrySet()) {
			CollectionTask storedQM = (CollectionTask) pairs.getValue();
			if (storedQM != null) {
				if (storedQM.equals(qm)) {
					if (qm.getStatusCode() != null) {
						if (!(qm.getStatusCode().equals(CollectionStatus.FATAL_ERROR.toString()))) {
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
		/*for (Map.Entry pairs : fbConfigMap.entrySet()) {
            CollectionTask storedTask = (CollectionTask) pairs.getValue();
            if (storedTask.equals(task)) {
                return true;
            }
        }*/
		return false;
	}

	// keeps track of tweets counters for various running collections
	public void incrCounter(String key, Long counter) {
		if (countersMap.containsKey(key)) {
			countersMap.put(key, countersMap.get(key) + counter);
		} else {
			countersMap.put(key, counter);
		}
	}

	public Long getCounterStatus(String key) {
		return countersMap.get(key);
	}

	public void deleteCounter(String key) {
		countersMap.remove(key);
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

	public Long getTotalCountSinceLastRestart() {

		Long totalCount = 0L;
		for (Map.Entry pairs : countersMap.entrySet()) {
			if(pairs.getValue() != null) {
				totalCount = totalCount +  (Long) pairs.getValue() ;
			}
		}
		return totalCount;
	}

	/*public List<String> getEligibleFacebookCollectionsToRun() {

    	List<String> collectionList = new ArrayList<String>();
    	Long runTime = null;

    	for(Map.Entry pair : this.fbConfigMap.entrySet()) {
    		FacebookCollectionTask task = (FacebookCollectionTask) pair.getValue();
    		if(task.getLastExecutionTime() != null) {
	    		runTime = task.getLastExecutionTime().getTime() + task.getFetchInterval();
	    		if(runTime >= new Date().getTime()) {
	    			collectionList.add((String) pair.getKey());
	    		}
    		}
    	}

    	return collectionList;
    }*/

	/*public Boolean getFbSyncObjMap(String key){
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
    }*/
}
