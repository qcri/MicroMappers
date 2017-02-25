package org.qcri.micromappers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.qcri.micromappers.utility.Status;

/**
 * @author Kushal
 *
 */
@Entity
@Table(name = "text_disambiguity_analysis")
public class TextDisambiguityAnalysis extends ExtendedBaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6322147699469534762L;

    @Column(name="collection_id")
    Long collectionId;
    
	@Enumerated(EnumType.STRING)
    @Column(name="status")
	private Status status;

    @Column(name="first_label_score",precision = 20, scale =20)
	private Double firstLabelScore;

    @Column(name="second_label_score",precision = 20, scale =20)
	private Double secondLabelScore;

    @Column(name="feed_id", length = 50, nullable = false)
    private String feedId;

    @Column(name="feed_text", length = 500)
	private String feedText;

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the firstLabelScore
	 */
	public Double getFirstLabelScore() {
		return firstLabelScore;
	}

	/**
	 * @param firstLabelScore the firstLabelScore to set
	 */
	public void setFirstLabelScore(Double firstLabelScore) {
		this.firstLabelScore = firstLabelScore;
	}

	/**
	 * @return the secondLabelScore
	 */
	public Double getSecondLabelScore() {
		return secondLabelScore;
	}

	/**
	 * @param secondLabelScore the secondLabelScore to set
	 */
	public void setSecondLabelScore(Double secondLabelScore) {
		this.secondLabelScore = secondLabelScore;
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
	 * @return the collectionId
	 */
	public Long getCollectionId() {
		return collectionId;
	}

	/**
	 * @param collectionId the collectionId to set
	 */
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * @return the feedText
	 */
	public String getFeedText() {
		return feedText;
	}

	/**
	 * @param feedText the feedText to set
	 */
	public void setFeedText(String feedText) {
		this.feedText = feedText;
	}

}
