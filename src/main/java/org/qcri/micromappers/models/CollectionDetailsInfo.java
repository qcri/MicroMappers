/**
 * 
 */
package org.qcri.micromappers.models;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.qcri.micromappers.entity.Collection;
import org.qcri.micromappers.entity.GlideMaster;
import org.qcri.micromappers.entity.GlobalEventDefinition;
import org.qcri.micromappers.utility.CollectionStatus;
import org.qcri.micromappers.utility.CollectionType;

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
	private CollectionStatus status;
	private boolean isTrashed;
    private String track;
    private String follow;
    private String geo;
    private String geoR;
    private String langFilters;
    private Integer durationHours;
    private int fetchInterval;
    private int fetchFrom;
    
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	public String getFollow() {
		return follow;
	}
	public void setFollow(String follow) {
		this.follow = follow;
	}
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	public String getGeoR() {
		return geoR;
	}
	public void setGeoR(String geoR) {
		this.geoR = geoR;
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
	public CollectionStatus getStatus() {
		return status;
	}
	public void setStatus(CollectionStatus status) {
		this.status = status;
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
		collection.setStatus(this.getStatus());
		collection.setProvider(CollectionType.valueOf(this.getProvider()));
		collection.setDurationHours(this.getDurationHours());
		collection.setFollow(this.getFollow());
		collection.setGeo(this.getGeo());
		collection.setGeoR(this.getGeoR());
		collection.setLangFilters(this.getLangFilters());
		collection.setFetchInterval(this.getFetchInterval());
		collection.setFetchFrom(this.getFetchFrom());
		
		if(StringUtils.isNotBlank(this.getTrack())) {
			collection.setTrack(this.getTrack().toLowerCase().trim());
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
	
}
