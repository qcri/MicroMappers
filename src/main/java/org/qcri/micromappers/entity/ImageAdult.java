package org.qcri.micromappers.entity;

import javax.persistence.*;

/**
 * Created by jlucas on 1/24/17.
 */
@Entity
@Table(name = "image_adult")
public class ImageAdult extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(columnDefinition = "boolean default false", nullable = false)
    Boolean isAdultContent;

    @Column(columnDefinition = "boolean default false", nullable = false)
    Boolean isRacyContent;

    @Column(name="adult_score")
    long adultScore;

    @Column(name="racy_score")
    long racyScore;

    @OneToOne
    @JoinColumn(name = "image_analysis_id", nullable=false)
    ImageAnalysis imageAnalysis;

    public ImageAdult() {
    }

    public ImageAdult(Boolean isAdultContent, Boolean isRacyContent, long adultScore, long racyScore, String source, String sourceId) {
        this.isAdultContent = isAdultContent;
        this.isRacyContent = isRacyContent;
        this.adultScore = adultScore;
        this.racyScore = racyScore;
    }

    public Boolean getIsAdultContent() {
        return isAdultContent;
    }

    public void setIsAdultContent(Boolean isAdultContent) {
        this.isAdultContent = isAdultContent;
    }

    public Boolean getIsRacyContent() {
        return isRacyContent;
    }

    public void setIsRacyContent(Boolean isRacyContent) {
        this.isRacyContent = isRacyContent;
    }

    public long getAdultScore() {
        return adultScore;
    }

    public void setAdultScore(long adultScore) {
        this.adultScore = adultScore;
    }

    public long getRacyScore() {
        return racyScore;
    }

    public void setRacyScore(long racyScore) {
        this.racyScore = racyScore;
    }

}
