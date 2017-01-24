package org.qcri.micromappers.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by jlucas on 1/24/17.
 */
@Entity
@Table(name = "image_analysis")
public class ImageAnalysis extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name = "img_url", length = 2000)
    String imageURL;

    @OneToOne
    @JoinColumn(name = "gdelt3w_id", nullable=true)
    Gdelt3W gdelt3W;

    @OneToOne
    @JoinColumn(name = "gdeltmmic_id", nullable=true)
    GdeltMMIC gdeltMMIC;

    @OneToOne(mappedBy = "imageAnalysis")
    ImageDescription imageDescription;

    @OneToOne(mappedBy = "imageAnalysis")
    ImageAdult imageAdult;

    @OneToMany(mappedBy = "imageAnalysis")
    Set<ImageCategory> imageCategory;

    @OneToMany(mappedBy = "imageAnalysis")
    Set<ImageTag> imageTag;

    public ImageAnalysis() {
    }

    public ImageAnalysis(String imageURL, Gdelt3W gdelt3W, GdeltMMIC gdeltMMIC, ImageDescription imageDescription, ImageAdult imageAdult, Set<ImageCategory> imageCategory, Set<ImageTag> imageTag) {
        this.imageURL = imageURL;
        this.gdelt3W = gdelt3W;
        this.gdeltMMIC = gdeltMMIC;
        this.imageDescription = imageDescription;
        this.imageAdult = imageAdult;
        this.imageCategory = imageCategory;
        this.imageTag = imageTag;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Gdelt3W getGdelt3W() {
        return gdelt3W;
    }

    public void setGdelt3W(Gdelt3W gdelt3W) {
        this.gdelt3W = gdelt3W;
    }

    public GdeltMMIC getGdeltMMIC() {
        return gdeltMMIC;
    }

    public void setGdeltMMIC(GdeltMMIC gdeltMMIC) {
        this.gdeltMMIC = gdeltMMIC;
    }

    public ImageDescription getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(ImageDescription imageDescription) {
        this.imageDescription = imageDescription;
    }

    public ImageAdult getImageAdult() {
        return imageAdult;
    }

    public void setImageAdult(ImageAdult imageAdult) {
        this.imageAdult = imageAdult;
    }

    public Set<ImageCategory> getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(Set<ImageCategory> imageCategory) {
        this.imageCategory = imageCategory;
    }

    public Set<ImageTag> getImageTag() {
        return imageTag;
    }

    public void setImageTag(Set<ImageTag> imageTag) {
        this.imageTag = imageTag;
    }
}
