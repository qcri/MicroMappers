/**
 * 
 */
package org.qcri.micromappers.models;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.CollectionLabel;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Kushal
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionDetailsInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6329919374565981089L;
	
	private Long id;
	private String code;
	private String name;
	private String provider;
	private GlobalEventDefinition globalEventDefinition;
	private GlideMaster glideMaster;
	private Long count;
	private String owner;
	private CollectionStatus twitterStatus;
	private CollectionStatus facebookStatus;
	private boolean isTrashed;
    private String track;
    private String subscribedProfiles;
    private String langFilters;
    private Integer durationHours;
    private int fetchInterval;
    private int fetchFrom;
    private Long twitterSinceDate;
    private Long twitterUntilDate;
    private boolean isComputerVisionEnabled;
    private CollectionLabel collectionLabel;
    
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	public String getLangFilters() {
		return langFilters;
	}
	public void setLangFilters(String langFilters) {
		this.langFilters = langFilters;
	}
	public Integer getDurationHours() {
		return durationHours;
	}
	public void setDurationHours(Integer durationHours) {
		this.durationHours = durationHours;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public boolean isTrashed() {
		return isTrashed;
	}
	public void setTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}
	public int getFetchInterval() {
		return fetchInterval;
	}
	public void setFetchInterval(int fetchInterval) {
		this.fetchInterval = fetchInterval;
	}
	public int getFetchFrom() {
		return fetchFrom;
	}
	public void setFetchFrom(int fetchFrom) {
		this.fetchFrom = fetchFrom;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
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
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@JsonIgnore
	public Collection toCollection() {
		Collection collection = new Collection();
		collection.setId(this.getId());
		collection.setCode(this.getCode());
		collection.setName(this.getName());
		if(this.getGlobalEventDefinition() != null){
			collection.setGlobalEventDefinition(this.getGlobalEventDefinition());
		}
		if(this.getGlideMaster() != null){
			collection.setGlideMaster(this.getGlideMaster());
		}
		//collection.setAccount(user);
		collection.setTwitterStatus(this.getTwitterStatus());
		collection.setFacebookStatus(this.getFacebookStatus());
		collection.setProvider(CollectionType.valueOf(this.getProvider()));
		collection.setDurationHours(this.getDurationHours());
		collection.setLangFilters(this.getLangFilters());
		collection.setFetchInterval(this.getFetchInterval());
		collection.setFetchFrom(this.getFetchFrom());
		collection.setComputerVisionEnabled(this.isComputerVisionEnabled());

		if(this.getTwitterSinceDate() != null) {
			collection.setTwitterSinceDate(new Date(this.getTwitterSinceDate()));
		}
		if(this.getTwitterUntilDate() != null){
			collection.setTwitterUntilDate(new Date(this.getTwitterUntilDate()));
		}

		if(this.getTwitterUntilDate() == null && this.getTwitterSinceDate() != null){
			collection.setTwitterUntilDate(new Date());
		}
		
		if(StringUtils.isNotBlank(this.getTrack())) {
			collection.setTrack(this.getTrack().toLowerCase().trim());
		}
		if(StringUtils.isNotBlank(this.getSubscribedProfiles())) {
			collection.setSubscribedProfiles(this.getSubscribedProfiles());
		}
		return collection;
	}
	/**
	 * @return the globalEventDefinition
	 */
	public GlobalEventDefinition getGlobalEventDefinition() {
		return globalEventDefinition;
	}
	/**
	 * @param globalEventDefinition the globalEventDefinition to set
	 */
	public void setGlobalEventDefinition(GlobalEventDefinition globalEventDefinition) {
		this.globalEventDefinition = globalEventDefinition;
	}
	/**
	 * @return the glideMaster
	 */
	public GlideMaster getGlideMaster() {
		return glideMaster;
	}
	/**
	 * @param glideMaster the glideMaster to set
	 */
	public void setGlideMaster(GlideMaster glideMaster) {
		this.glideMaster = glideMaster;
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
	 * @return the isComputerVisionEnabled
	 */
	public boolean isComputerVisionEnabled() {
		return isComputerVisionEnabled;
	}
	/**
	 * @param isComputerVisionEnabled the isComputerVisionEnabled to set
	 */
	public void setComputerVisionEnabled(boolean isComputerVisionEnabled) {
		this.isComputerVisionEnabled = isComputerVisionEnabled;
	}
	/**
	 * @return the twitterSinceDate
	 */
	public Long getTwitterSinceDate() {
		return twitterSinceDate;
	}
	/**
	 * @param twitterSinceDate the twitterSinceDate to set
	 */
	public void setTwitterSinceDate(Long twitterSinceDate) {
		this.twitterSinceDate = twitterSinceDate;
	}
	/**
	 * @return the twitterUntilDate
	 */
	public Long getTwitterUntilDate() {
		return twitterUntilDate;
	}
	/**
	 * @param twitterUntilDate the twitterUntilDate to set
	 */
	public void setTwitterUntilDate(Long twitterUntilDate) {
		this.twitterUntilDate = twitterUntilDate;
	}
	/**
	 * @return the collectionLabel
	 */
	public CollectionLabel getCollectionLabel() {
		return collectionLabel;
	}
	/**
	 * @param collectionLabel the collectionLabel to set
	 */
	public void setCollectionLabel(CollectionLabel collectionLabel) {
		this.collectionLabel = collectionLabel;
	}
}
