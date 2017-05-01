package org.qcri.micromappers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.qcri.micromappers.models.GdeltImageClassifierProfile;

import javax.persistence.*;

/**
 * Created by jlucas on 4/30/17.
 */
@Entity
@Table(name = "gdeltImageClassifier")
public class GdeltImageClassifier extends ExtendedBaseEntity  {

    private static final long serialVersionUID = 1705153546973813717L;

    @Column(name="name")
    private String name;

    @Column(name="theme")
    private String theme;

    @Column(name="tone")
    private int tone;

    @Column(name="timeSpan")
    private int timeSpan;

    @Column(name="location")
    private String location;

    @Column(name="locationCC")
    private String locationCC;

    @Column(name="imageWebTag")
    private String imageWebTag;

    @Column(name="imageTag")
    private String imageTag;

    @Column(name = "state", nullable = false, length = 50)
    private String state;

    @OneToOne
    @JoinColumn(name = "account_id", nullable=false)
    private Account account;

    @Transient
    private GdeltImageClassifierProfile gdeltImageClassifierProfile;

    public GdeltImageClassifier() {
    }

    public GdeltImageClassifier(String name, String theme, String location, String locationCC, String imageWebTag, String imageTag, Account account, String state) {
        this.name = name;
        this.theme = theme;
        this.location = location;
        this.locationCC = locationCC;
        this.imageWebTag = imageWebTag;
        this.imageTag = imageTag;
        this.account = account;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getTone() {
        return tone;
    }

    public void setTone(int tone) {
        this.tone = tone;
    }

    public int getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationCC() {
        return locationCC;
    }

    public void setLocationCC(String locationCC) {
        this.locationCC = locationCC;
    }

    public String getImageWebTag() {
        return imageWebTag;
    }

    public void setImageWebTag(String imageWebTag) {
        this.imageWebTag = imageWebTag;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setGdeltImageClassifierProfile(){
        GdeltImageClassifierProfile gdeltImageClassifierProfile =
                new GdeltImageClassifierProfile(this.theme, this.location, this.locationCC,this.imageWebTag,this.imageTag);

        gdeltImageClassifierProfile.setGeoJsonQuery();
        gdeltImageClassifierProfile.setHtmlQuery();
        gdeltImageClassifierProfile.setImageGeoJsonQuery();
        gdeltImageClassifierProfile.setImageHtmlQuery();

        this.gdeltImageClassifierProfile = gdeltImageClassifierProfile;
    }
}
