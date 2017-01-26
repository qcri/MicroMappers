package org.qcri.micromappers.entity;

import javax.persistence.*;

/**
 * Created by jlucas on 1/25/17.
 */
@Entity
@Table(name = "image_analyser_task")
public class ImageAnalyserTask extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name="state")
    String state;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable=false)
    Collection collection;

    @OneToOne
    @JoinColumn(name = "data_feed_id", nullable=true)
    DataFeed dataFeed;

    @OneToOne
    @JoinColumn(name = "gdelt3w_id", nullable=true)
    Gdelt3W gdelt3W;

    @OneToOne
    @JoinColumn(name = "gdeltmmic_id", nullable=true)
    GdeltMMIC gdeltMMIC;


    public ImageAnalyserTask() {
    }

    public ImageAnalyserTask(String state, Collection collection) {
        this.state = state;
        this.collection = collection;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public DataFeed getDataFeed() {
        return dataFeed;
    }

    public void setDataFeed(DataFeed dataFeed) {
        this.dataFeed = dataFeed;
    }

    public GdeltMMIC getGdeltMMIC() {
        return gdeltMMIC;
    }

    public void setGdeltMMIC(GdeltMMIC gdeltMMIC) {
        this.gdeltMMIC = gdeltMMIC;
    }

    public Gdelt3W getGdelt3W() {
        return gdelt3W;
    }

    public void setGdelt3W(Gdelt3W gdelt3W) {
        this.gdelt3W = gdelt3W;
    }
}
