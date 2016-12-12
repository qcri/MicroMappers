/**
 * 
 */
package org.qcri.micromappers.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.qcri.micromappers.utility.CollectionStatus;

/**
 * @author Kushal
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionDetailsInfo {

	private String code;
	private String name;
	private String lastDocument;
	private String provider;
	private Long count;
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
	public String getLastDocument() {
		return lastDocument;
	}
	public void setLastDocument(String lastDocument) {
		this.lastDocument = lastDocument;
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
}
