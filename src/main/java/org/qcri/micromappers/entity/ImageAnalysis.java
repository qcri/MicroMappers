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

    @Column(name = "state", length = 1000)
    String state;

    @OneToOne(mappedBy = "imageAnalysis", cascade={CascadeType.ALL})
    ImageAdult imageAdult;

    @OneToMany(mappedBy = "imageAnalysis", cascade={CascadeType.ALL})
    Set<ImageDescription> imageDescription;

    @OneToMany(mappedBy = "imageAnalysis", cascade={CascadeType.ALL})
    Set<ImageClassify> imageClassify;

    @OneToMany(mappedBy = "imageAnalysis", cascade={CascadeType.ALL})
    Set<ImageTag> imageTag;

    public ImageAnalysis() {
    }

    public ImageAnalysis(String imageURL, GdeltMMIC gdeltMMIC,  ImageAdult imageAdult) {
        this.imageURL = imageURL;
        this.gdeltMMIC = gdeltMMIC;
        this.imageAdult = imageAdult;
        this.state = "OK";
    }

    public ImageAnalysis(String imageURL, Gdelt3W gdelt3W, GdeltMMIC gdeltMMIC, String state,
                         ImageAdult imageAdult, Set<ImageDescription> imageDescription,
                         Set<ImageClassify> imageClassify, Set<ImageTag> imageTag) {
        this.imageURL = imageURL;
        this.gdelt3W = gdelt3W;
        this.gdeltMMIC = gdeltMMIC;
        this.state = state;
        this.imageAdult = imageAdult;
        this.imageDescription = imageDescription;
        this.imageClassify = imageClassify;
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

    public ImageAdult getImageAdult() {
        return imageAdult;
    }

    public void setImageAdult(ImageAdult imageAdult) {
        this.imageAdult = imageAdult;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<ImageDescription> getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(Set<ImageDescription> imageDescription) {
        this.imageDescription = imageDescription;
    }

    public Set<ImageTag> getImageTag() {
        return imageTag;
    }

    public void setImageTag(Set<ImageTag> imageTag) {
        this.imageTag = imageTag;
    }

    public Set<ImageClassify> getImageClassify() {
        return imageClassify;
    }

    public void setImageClassify(Set<ImageClassify> imageClassify) {
        this.imageClassify = imageClassify;
    }
}
