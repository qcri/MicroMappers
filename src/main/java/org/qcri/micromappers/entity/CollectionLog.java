package org.qcri.micromappers.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "collection_log")
public class CollectionLog extends ExtendedBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7919567101104671503L;

	public CollectionLog() {
		super();
	}

	public CollectionLog(Collection collection) {
		super();
		this.collection = collection;
		this.geo = collection.getGeo();
		this.track = collection.getTrack();
		this.langFilters = collection.getLangFilters();
		this.follow = collection.getFollow();
	}

	@ManyToOne
	@JoinColumn(name="collection_id", nullable = false)
	private Collection collection;

	@Column(name = "count", columnDefinition = "bigint default 0")
	private Long count;

	@Column(length = 5000, name = "track")
	private String track;

	@Column(length=1000, name="follow")
	private String follow;

	@Column(length=1000, name="geo")
	private String geo;

	@Column(name="lang_filters")
	private String langFilters;

	@Column(name="start_date", nullable = false)
	private Date startDate;

	@Column(name="end_date")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name="updated_by", nullable = false)
	private Account updatedBy;

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
}
