package org.qcri.micromappers.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.qcri.micromappers.utility.TextAnalyticsStatus;

/**
 * Created by jlucas on 2/17/17.
 */
@Entity
@Table(name = "sentiment_analysis")
public class SentimentAnalysis extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
	private TextAnalyticsStatus state;

    @Column(name="positive",precision = 20, scale =20)
    BigDecimal positive;

    @Column(name="negative",precision = 20, scale =20)
    BigDecimal negative;

    @Column(length = 50, name="feed_id", nullable = false)
    private String feedId;

    @Column(length = 100, name="feed_path", nullable = false)
    private String feedPath;
    
    @Column(length = 500, name="feed_text")
    private String feedText;

    @Column(name="collection_id")
    Long collectionId;

    public SentimentAnalysis() {
    }

    public SentimentAnalysis(TextAnalyticsStatus state, BigDecimal positive, BigDecimal negative, String feedId, long collectionId) {
        this.setState(state);
        this.positive = positive;
        this.negative = negative;
        this.feedId = feedId;
        this.collectionId = collectionId;
    }

    public String getFeedText() {
        return feedText;
    }

    public void setFeedText(String feedText) {
        this.feedText = feedText;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public BigDecimal getPositive() {
        return positive;
    }

    public void setPositive(BigDecimal positive) {
        this.positive = positive;
    }

    public BigDecimal getNegative() {
        return negative;
    }

    public void setNegative(BigDecimal negative) {
        this.negative = negative;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

	/**
	 * @return the state
	 */
	public TextAnalyticsStatus getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(TextAnalyticsStatus state) {
		this.state = state;
	}

	/**
	 * @return the feedPath
	 */
	public String getFeedPath() {
		return feedPath;
	}

	/**
	 * @param feedPath the feedPath to set
	 */
	public void setFeedPath(String feedPath) {
		this.feedPath = feedPath;
	}
}
