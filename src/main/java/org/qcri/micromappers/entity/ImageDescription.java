package org.qcri.micromappers.entity;

import javax.persistence.*;

/**
 * Created by jlucas on 1/24/17.
 */
@Entity
@Table(name = "image_description")
public class ImageDescription extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name = "tags", length = 6000)
    String tags;

    @Column(name = "text", length = 3000)
    String text;

    @Column(name = "confidence")
    long confidence;

    @OneToOne
    @JoinColumn(name = "image_analysis_id", nullable=false)
    ImageAnalysis imageAnalysis;

    public ImageDescription() {
    }

    public ImageDescription(String tags, String text, long confidence, ImageAnalysis imageAnalysis) {
        this.tags = tags;
        this.text = text;
        this.confidence = confidence;
        this.imageAnalysis = imageAnalysis;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getConfidence() {
        return confidence;
    }

    public void setConfidence(long confidence) {
        this.confidence = confidence;
    }

    public ImageAnalysis getImageAnalysis() {
        return imageAnalysis;
    }

    public void setImageAnalysis(ImageAnalysis imageAnalysis) {
        this.imageAnalysis = imageAnalysis;
    }
}
