package org.qcri.micromappers.models;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A JAVA POJO class used to define a collection base class for Twitter/Facebook collection.
 * @author Kushal 
 */
public class CollectionTask {

	private Long id;
	protected String collectionCode;
	protected String collectionName;
	protected String toTrack;
	private String subscribedProfiles;
	private CollectionStatus twitterStatus;
	private CollectionStatus facebookStatus;
	protected String statusMessage;
	protected String accessToken;
	protected String accessTokenSecret;
	private Long tweetCount;
	private Long fbPostCount;
	protected String languageFilter;
	protected CollectionType provider;
	protected Date lastExecutionTime;
	private Date twitterLastExecutionTime;
	private Date twitterSinceDate;
	private Date twitterUntilDate;
	protected Integer fetchInterval;
	protected Integer fetchFrom;

	public CollectionTask() {
	}

	/**
	 * @return the toTrack
	 */
	public String getToTrack() {
		return toTrack;
	}

	/**
	 * @param toTrack
	 *            the toTrack to set
	 */
	public void setToTrack(String toTrack) {
		this.toTrack = toTrack;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken
	 *            the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the accessTokenSecret
	 */
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	/**
	 * @param accessTokenSecret
	 *            the accessTokenSecret to set
	 */
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collectionName
	 *            the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	/**
	 * @return the collectionCode
	 */
	public String getCollectionCode() {
		return collectionCode;
	}

	/**
	 * @param collectionCode
	 *            the collectionCode to set
	 */
	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	/**
	 * @return the statusMessage
	 */
	public String getStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage
	 *            the statusMessage to set
	 */
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * @return the languageFilter
	 */
	public String getLanguageFilter() {
		return languageFilter;
	}

	/**
	 * @param languageFilter
	 *            the languageFilter to set
	 */
	public void setLanguageFilter(String languageFilter) {
		this.languageFilter = languageFilter;
	}

	/**
	 * @return the lastExecutionTime
	 */
	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}

	/**
	 * @param lastExecutionTime
	 *            the lastExecutionTime to set
	 */
	public void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	/**
	 * @return the fetchInterval
	 */
	public int getFetchInterval() {
		return fetchInterval;
	}

	/**
	 * @param fetchInterval
	 *            the fetchInterval to set
	 */
	public void setFetchInterval(int fetchInterval) {
		this.fetchInterval = fetchInterval;
	}

	/**
	 * @return the fetchFrom
	 */
	public int getFetchFrom() {
		return fetchFrom;
	}

	/**
	 * @param fetchFrom
	 *            the fetchFrom to set
	 */
	public void setFetchFrom(int fetchFrom) {
		this.fetchFrom = fetchFrom;
	}

	/**
	 * @return the provider
	 */
	public CollectionType getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(CollectionType provider) {
		this.provider = provider;
	}

	@JsonIgnore
	@Override
	public CollectionTask clone() {

		CollectionTask newTask = new CollectionTask();
		newTask.setId(id);
		newTask.setAccessToken(accessToken);
		newTask.setAccessTokenSecret(accessTokenSecret);
		newTask.setCollectionCode(collectionCode);
		newTask.setCollectionName(collectionName);
		newTask.setTwitterStatus(twitterStatus);
		newTask.setFacebookStatus(facebookStatus);
		newTask.setStatusMessage(statusMessage);
		newTask.setToTrack(toTrack);
		newTask.setSubscribedProfiles(subscribedProfiles);
		newTask.setTweetCount(tweetCount);
		newTask.setFbPostCount(fbPostCount);
		newTask.setLanguageFilter(languageFilter);
		newTask.setLastExecutionTime(lastExecutionTime);
		newTask.setFetchInterval(fetchInterval);
		newTask.setFetchFrom(fetchFrom);
		newTask.setProvider(provider);
		newTask.setTwitterLastExecutionTime(twitterLastExecutionTime);
		newTask.setTwitterSinceDate(twitterSinceDate);
		newTask.setTwitterUntilDate(twitterUntilDate);
		return newTask;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final CollectionTask other = (CollectionTask) obj;
		if ((this.accessToken == null) ? (other.accessToken != null) : !this.accessToken.equals(other.accessToken)) {
			return false;
		}
		if ((this.accessTokenSecret == null) ? (other.accessTokenSecret != null)
				: !this.accessTokenSecret.equals(other.accessTokenSecret)) {
			return false;
		}
		return true;
	}

	@JsonIgnore
	public boolean isToTrackAvailable() {
		if (StringUtils.isNotBlank(toTrack)) {
			return true;
		} else {
			return false;
		}
	}
	
	@JsonIgnore
	public boolean isSubscribedProfilesAvailable() {
		if (StringUtils.isNotBlank(subscribedProfiles)) {
			return true;
		} else {
			return false;
		}
	}

	@JsonIgnore
	public boolean checkSocialConfigInfo() {

		boolean isConfigured = StringUtils.isNotBlank(getAccessToken());

		if (getProvider().equals("Twitter")) {
			isConfigured = isConfigured && StringUtils.isNotBlank(getAccessTokenSecret());
		}

		return isConfigured;

	}

	/**
	 * @return the subscribedProfiles
	 */
	public String getSubscribedProfiles() {
		return subscribedProfiles;
	}

	/**
	 * @param subscribedProfiles the subscribedProfiles to set
	 */
	public void setSubscribedProfiles(String subscribedProfiles) {
		this.subscribedProfiles = subscribedProfiles;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the twitterStatus
	 */
	public CollectionStatus getTwitterStatus() {
		return twitterStatus;
	}

	/**
	 * @param twitterStatus the twitterStatus to set
	 */
	public void setTwitterStatus(CollectionStatus twitterStatus) {
		this.twitterStatus = twitterStatus;
	}

	/**
	 * @return the facebookStatus
	 */
	public CollectionStatus getFacebookStatus() {
		return facebookStatus;
	}

	/**
	 * @param facebookStatus the facebookStatus to set
	 */
	public void setFacebookStatus(CollectionStatus facebookStatus) {
		this.facebookStatus = facebookStatus;
	}

	/**
	 * @return the tweetCount
	 */
	public Long getTweetCount() {
		return tweetCount;
	}

	/**
	 * @param tweetCount the tweetCount to set
	 */
	public void setTweetCount(Long tweetCount) {
		this.tweetCount = tweetCount;
	}

	/**
	 * @return the fbPostCount
	 */
	public Long getFbPostCount() {
		return fbPostCount;
	}

	/**
	 * @param fbPostCount the fbPostCount to set
	 */
	public void setFbPostCount(Long fbPostCount) {
		this.fbPostCount = fbPostCount;
	}

	/**
	 * @return the twitterSinceDate
	 */
	public Date getTwitterSinceDate() {
		return twitterSinceDate;
	}

	/**
	 * @param twitterSinceDate the twitterSinceDate to set
	 */
	public void setTwitterSinceDate(Date twitterSinceDate) {
		this.twitterSinceDate = twitterSinceDate;
	}

	/**
	 * @return the twitterUntilDate
	 */
	public Date getTwitterUntilDate() {
		return twitterUntilDate;
	}

	/**
	 * @param twitterUntilDate the twitterUntilDate to set
	 */
	public void setTwitterUntilDate(Date twitterUntilDate) {
		this.twitterUntilDate = twitterUntilDate;
	}

	/**
	 * @return the twitterLastExecutionTime
	 */
	public Date getTwitterLastExecutionTime() {
		return twitterLastExecutionTime;
	}

	/**
	 * @param twitterLastExecutionTime the twitterLastExecutionTime to set
	 */
	public void setTwitterLastExecutionTime(Date twitterLastExecutionTime) {
		this.twitterLastExecutionTime = twitterLastExecutionTime;
	}
}
