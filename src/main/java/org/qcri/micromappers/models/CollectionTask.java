package org.qcri.micromappers.models;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.qcri.micromappers.utility.CollectionType;

/**
 * @author Kushal A JAVA POJO class used to define a collection base class for
 *         Twitter/Facebook collection.
 */
public class CollectionTask {

	protected String collectionCode;
	protected String collectionName;
	protected String toTrack;
	protected String lastDocument;
	protected String statusCode;
	protected String statusMessage;
	protected String accessToken;
	protected String accessTokenSecret;
	protected Long collectionCount;
	protected CollectionType provider;
	protected String toFollow;
	protected String geoLocation;
	protected String geoR;
	protected String languageFilter;
	protected Date lastExecutionTime;
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
	 * @return the collectionCount
	 */
	public Long getCollectionCount() {
		return collectionCount;
	}

	/**
	 * @param collectionCount
	 *            the collectionCount to set
	 */
	public void setCollectionCount(Long collectionCount) {
		this.collectionCount = collectionCount;
	}

	/**
	 * @return the lastDocument
	 */
	public String getLastDocument() {
		return lastDocument;
	}

	/**
	 * @param lastDocument
	 *            the lastDocument to set
	 */
	public void setLastDocument(String lastDocument) {
		this.lastDocument = lastDocument;
	}

	/**
	 * @return the status
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatusCode(String status) {
		this.statusCode = status;
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
	 * @return the toFollow
	 */
	public String getToFollow() {
		return toFollow;
	}

	/**
	 * @param toFollow
	 *            the toFollow to set
	 */
	public void setToFollow(String toFollow) {
		this.toFollow = toFollow;
	}

	/**
	 * @return the geoLocation
	 */
	public String getGeoLocation() {
		return geoLocation;
	}

	/**
	 * @param geoLocation
	 *            the geoLocation to set
	 */
	public void setGeoLocation(String geoLocation) {
		this.geoLocation = geoLocation;
	}

	/**
	 * @return the geoR
	 */
	public String getGeoR() {
		return geoR;
	}

	/**
	 * @param geoR
	 *            the geoR to set
	 */
	public void setGeoR(String geoR) {
		this.geoR = geoR;
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

	@Override
	public CollectionTask clone() {

		CollectionTask newTask = new CollectionTask();
		newTask.setAccessToken(accessToken);
		newTask.setAccessTokenSecret(accessTokenSecret);
		newTask.setCollectionCode(collectionCode);
		newTask.setCollectionName(collectionName);
		newTask.setLastDocument(lastDocument);
		newTask.setStatusCode(statusCode);
		newTask.setStatusMessage(statusMessage);
		newTask.setToTrack(toTrack);
		newTask.setToFollow(toFollow);
		newTask.setCollectionCount(collectionCount);
		newTask.setGeoLocation(geoLocation);
		newTask.setGeoR(geoR);
		newTask.setLanguageFilter(languageFilter);
		newTask.setLastExecutionTime(lastExecutionTime);
		newTask.setFetchInterval(fetchInterval);
		newTask.setFetchFrom(fetchFrom);
		newTask.setProvider(provider);
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

	public boolean isToTrackAvailable() {
		if (StringUtils.isNotBlank(toTrack)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkSocialConfigInfo() {

		boolean isConfigured = StringUtils.isNotEmpty(getAccessToken());

		if (getProvider().equals("Twitter")) {
			isConfigured = isConfigured && StringUtils.isNotEmpty(getAccessTokenSecret());
		}

		return isConfigured;

	}

	public boolean isToFollowAvailable() {
		if (StringUtils.isNotBlank(toFollow)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isGeoLocationAvailable() {
		if (StringUtils.isNotBlank(geoLocation)) {
			return true;
		} else {
			return false;
		}
	}
}
