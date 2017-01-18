package org.qcri.micromappers.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.qcri.micromappers.models.CollectionDetailsInfo;
import org.qcri.micromappers.models.CollectionTask;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;

@Entity
@Table(name = "collection")
public class Collection extends ExtendedBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1705153546973813717L;

	@Column(length = 64, unique = true)
	private String code;

	@Column(length = 255, unique = true)
	private String name;

	@ManyToOne
	@JoinColumn(name = "account_id", nullable=false)
	private Account account;

	@ManyToOne
	@JoinColumn(name = "global_event_definition_id", nullable=true)
	private GlobalEventDefinition globalEventDefinition;
	
	@ManyToOne
	@JoinColumn(name = "glide_master_id", nullable=true)
	private GlideMaster glideMaster;

	@Enumerated(EnumType.ORDINAL)
	private CollectionStatus status;

	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private CollectionType provider;

	@Column(name="duration_hours", columnDefinition = "int default 48")
	private Integer durationHours;

	@Column(name="is_trashed")
	private boolean isTrashed;

	@Column(length = 5000, name = "track")
	private String track;

	@Column(length = 1000, name = "follow")
	private String follow;

	@Column(length = 1000, name = "geo")
	private String geo;

	@Column(name="geo_r")
	private String geoR;

	@Column(name="lang_filters")
	private String langFilters;

	@Column(name="fetch_interval", columnDefinition="int default 0")
	private Integer fetchInterval;

	//default value 7 days = 24 * 7 hours
	@Column(name="fetch_from", columnDefinition="int default 168")
	private Integer fetchFrom;

	@Column(name="last_execution_time")
	private Date lastExecutionTime;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return the status
	 */
	public CollectionStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(CollectionStatus status) {
		this.status = status;
	}

	/**
	 * @return the provider
	 */
	public CollectionType getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(CollectionType provider) {
		this.provider = provider;
	}

	/**
	 * @return the durationHours
	 */
	public Integer getDurationHours() {
		return durationHours;
	}

	/**
	 * @param durationHours the durationHours to set
	 */
	public void setDurationHours(Integer durationHours) {
		this.durationHours = durationHours;
	}

	/**
	 * @return the isTrashed
	 */
	public boolean isTrashed() {
		return isTrashed;
	}

	/**
	 * @param isTrashed the isTrashed to set
	 */
	public void setTrashed(boolean isTrashed) {
		this.isTrashed = isTrashed;
	}

	/**
	 * @return the track
	 */
	public String getTrack() {
		return track;
	}

	/**
	 * @param track the track to set
	 */
	public void setTrack(String track) {
		this.track = track;
	}

	/**
	 * @return the follow
	 */
	public String getFollow() {
		return follow;
	}

	/**
	 * @param follow the follow to set
	 */
	public void setFollow(String follow) {
		this.follow = follow;
	}

	/**
	 * @return the geo
	 */
	public String getGeo() {
		return geo;
	}

	/**
	 * @param geo the geo to set
	 */
	public void setGeo(String geo) {
		this.geo = geo;
	}

	/**
	 * @return the geoR
	 */
	public String getGeoR() {
		return geoR;
	}

	/**
	 * @param geoR the geoR to set
	 */
	public void setGeoR(String geoR) {
		this.geoR = geoR;
	}

	/**
	 * @return the langFilters
	 */
	public String getLangFilters() {
		return langFilters;
	}

	/**
	 * @param langFilters the langFilters to set
	 */
	public void setLangFilters(String langFilters) {
		this.langFilters = langFilters;
	}

	/**
	 * @return the lastExecutionTime
	 */
	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}

	/**
	 * @param lastExecutionTime the lastExecutionTime to set
	 */
	public void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}

	/**
	 * @return the fetchInterval
	 */
	public Integer getFetchInterval() {
		return fetchInterval;
	}

	/**
	 * @param fetchInterval the fetchInterval to set
	 */
	public void setFetchInterval(Integer fetchInterval) {
		this.fetchInterval = fetchInterval;
	}

	/**
	 * @return the fetchFrom
	 */
	public Integer getFetchFrom() {
		return fetchFrom;
	}

	/**
	 * @param fetchFrom the fetchFrom to set
	 */
	public void setFetchFrom(Integer fetchFrom) {
		this.fetchFrom = fetchFrom;
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
	
	public CollectionTask toCollectionTask(UserConnection userConnection) {
		CollectionTask task = new CollectionTask();
		if(userConnection !=null){
			task.setAccessToken(userConnection.getAccessToken());
			task.setAccessTokenSecret(userConnection.getSecret());
		}

		task.setCollectionName(this.getName());
		task.setCollectionCode(this.getCode());
		task.setToFollow(this.getFollow());
		task.setToTrack(this.getTrack());
		task.setGeoLocation(this.getGeo());
		task.setGeoR(this.getGeoR());
		task.setLanguageFilter(this.getLangFilters());
		task.setFetchInterval(this.getFetchInterval());
		task.setProvider(this.getProvider());
		task.setFetchInterval(this.getFetchInterval());
		task.setFetchFrom(this.getFetchFrom());
		task.setLastExecutionTime(this.getLastExecutionTime());
		task.setStatusCode(this.getStatus());
		return task;
	}
	
	public CollectionDetailsInfo toCollectionDetailsInfo() {
		CollectionDetailsInfo collectionDetailsInfo = new CollectionDetailsInfo();
		collectionDetailsInfo.setId(this.getId());
		collectionDetailsInfo.setCode(this.getCode());
		collectionDetailsInfo.setDurationHours(this.getDurationHours());
		collectionDetailsInfo.setFetchFrom(this.getFetchFrom());
		collectionDetailsInfo.setFollow(this.getFollow());
		collectionDetailsInfo.setGeo(this.getGeo());
		collectionDetailsInfo.setGeoR(this.getGeoR());
		if(this.getGlobalEventDefinition() != null){
			collectionDetailsInfo.setGlobalEventDefinition(this.getGlobalEventDefinition());
		}
		if(this.getGlideMaster() != null){
			collectionDetailsInfo.setGlideMaster(this.getGlideMaster());
		}
		collectionDetailsInfo.setLangFilters(this.getLangFilters());
		collectionDetailsInfo.setName(this.getName());
		collectionDetailsInfo.setOwner(this.getAccount().getUserName());
		collectionDetailsInfo.setProvider(this.getProvider().toString());
		collectionDetailsInfo.setStatus(this.getStatus());
		collectionDetailsInfo.setTrack(this.getTrack());
		collectionDetailsInfo.setTrashed(this.isTrashed);

		
		return collectionDetailsInfo;

	}

}