package org.qcri.micromappers.entity;

import javax.persistence.*;

/**
 * Created by jlucas on 1/24/17.
 */
@Entity
@Table(name="image_tag")
public class ImageTag extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name = "name", length = 500)
    String name;

    @Column(name = "confidence")
    long confidence;

    @ManyToOne
    @JoinColumn(name="image_analysis_id", nullable = false)
    private ImageAnalysis imageAnalysis;

    public ImageTag() {
    }

    public ImageTag(String name, long confidence, ImageAnalysis imageAnalysis) {
        this.name = name;
        this.confidence = confidence;
        this.imageAnalysis = imageAnalysis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
