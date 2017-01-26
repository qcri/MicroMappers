package org.qcri.micromappers.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.qcri.micromappers.utility.CollectionType;

@Entity
@Table(name = "collection_log")
public class CollectionLog extends ExtendedBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7919567101104671503L;

	@ManyToOne
	@JoinColumn(name="collection_id", nullable = false)
	private Collection collection;

	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private CollectionType provider;
	
	@Column(name = "count", columnDefinition = "bigint default 0")
	private Long count;

	@Column(length = 5000, name = "track")
	private String track;

	@Column(length=5000, name="subscribedProfiles")
	private String subscribedProfiles;

	@Column(name="lang_filters")
	private String langFilters;

	@Column(name="start_date", nullable = false)
	private Date startDate;

	@Column(name="end_date")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name="updated_by", nullable = false)
	private Account updatedBy;

	public CollectionLog() {
		super();
	}

	public CollectionLog(Collection collection) {
		super();
		this.collection = collection;
		this.track = collection.getTrack();
		this.langFilters = collection.getLangFilters();
		this.subscribedProfiles = collection.getSubscribedProfiles();
	}
	
	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the langFilter
	 */
	public String getLangFilters() {
		return langFilters;
	}

	/**
	 * @param langFilter the langFilter to set
	 */
	public void setLangFilters(String langFilter) {
		this.langFilters = langFilter;
	}

	/**
	 * @return the collection
	 */
	public Collection getCollection() {
		return collection;
	}

	/**
	 * @param collection the collection to set
	 */
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
	/**
	 * @return the updatedBy
	 */
	public Account getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(Account updatedBy) {
		this.updatedBy = updatedBy;
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
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
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
}
