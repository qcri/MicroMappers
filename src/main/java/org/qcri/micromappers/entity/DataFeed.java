package org.qcri.micromappers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.qcri.micromappers.utility.CollectionType;

@Entity
@Table(name="data_feed", uniqueConstraints={@UniqueConstraint(columnNames = {"collection_id", "feed_id"})})
public class DataFeed extends ExtendedBaseEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4815597105696055196L;
	
	@ManyToOne
	@JoinColumn(name = "collection_id", nullable=false)
	private Collection collection;
	
	@Column(length = 10, nullable = false)
	@Enumerated(EnumType.STRING)
	private CollectionType provider;
	
	@Column(length = 50, name="feed_id")
	private String feedId;
	
	@ManyToOne
	@JoinColumn(name="parent_feed_id")
	private DataFeed parentFeed;

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
	 * @return the parentFeed
	 */
	public DataFeed getParentFeed() {
		return parentFeed;
	}

	/**
	 * @param parentFeed the parentFeed to set
	 */
	public void setParentFeed(DataFeed parentFeed) {
		this.parentFeed = parentFeed;
	}

	/**
	 * @return the feedId
	 */
	public String getFeedId() {
		return feedId;
	}

	/**
	 * @param feedId the feedId to set
	 */
	public void setFeedId(String feedId) {
		this.feedId = feedId;
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
}
