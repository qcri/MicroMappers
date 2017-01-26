package org.qcri.micromappers.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by jlucas on 1/24/17.
 */
@Entity
@Table(name = "image_adult")
public class ImageAdult extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name="is_adult_content",columnDefinition = "boolean default false")
    Boolean isAdultContent;

    @Column(name="is_racy_content",columnDefinition = "boolean default false")
    Boolean isRacyContent;

    @Column(name="adult_score",precision = 20, scale =20)
    BigDecimal adultScore;

    @Column(name="racy_score", precision = 20, scale =20)
    BigDecimal racyScore;

    @OneToOne
    @JoinColumn(name = "image_analysis_id", nullable=false)
    ImageAnalysis imageAnalysis;

    public ImageAdult() {
    }

    public ImageAdult(Boolean isAdultContent, Boolean isRacyContent, BigDecimal adultScore, BigDecimal racyScore) {
        this.isAdultContent = isAdultContent;
        this.isRacyContent = isRacyContent;
        this.adultScore = adultScore;
        this.racyScore = racyScore;
    }

    public ImageAdult(Boolean isAdultContent, Boolean isRacyContent, BigDecimal adultScore, BigDecimal racyScore, ImageAnalysis imageAnalysis) {
        this.isAdultContent = isAdultContent;
        this.isRacyContent = isRacyContent;
        this.adultScore = adultScore;
        this.racyScore = racyScore;
        this.imageAnalysis = imageAnalysis;
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

    public BigDecimal getAdultScore() {
        return adultScore;
    }

    public void setAdultScore(BigDecimal adultScore) {
        this.adultScore = adultScore;
    }

    public BigDecimal getRacyScore() {
        return racyScore;
    }

    public void setRacyScore(BigDecimal racyScore) {
        this.racyScore = racyScore;
    }

    public ImageAnalysis getImageAnalysis() {
        return imageAnalysis;
    }

    public void setImageAnalysis(ImageAnalysis imageAnalysis) {
        this.imageAnalysis = imageAnalysis;
    }
}
