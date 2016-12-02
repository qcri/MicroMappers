package org.qcri.micromappers.model;

import javax.persistence.*;

/**
 * Created by jlucas on 11/28/16.
 */
@Entity
@Table(name = "gdeltmmic")
public class GdeltMMIC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gdeltmmic_id;

    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "article_url", nullable = false)
    private String articleURL;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "location", nullable = true)
    private String location;

    @Column(name = "lat", nullable = true)
    private String lat;

    @Column(name = "lon", nullable = true)
    private String lon;

    @Column(name = "img_url", nullable = false)
    private String imgURL;

    @Column(name = "glide_code", nullable = false)
    private String glideCode;

    @Column(name = "local_img_url", nullable = false)
    private String localImgUrl;

    @Column(name = "local_article_url", nullable = false)
    private String localArticleUrl;

    @Column(name = "state", nullable = false)
    private String state;

    public GdeltMMIC() {
    }

    public GdeltMMIC(String languageCode, String articleURL, String timestamp, String location, String lat, String lon, String imgURL, String glideCode) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.timestamp = timestamp;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
        this.imgURL = imgURL;
        this.glideCode = glideCode;
        this.state = "insert";
    }

    public GdeltMMIC(String languageCode, String articleURL, String timestamp, String location, String lat, String lon, String imgURL, String glideCode, String localImgUrl, String localArticleUrl, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.timestamp = timestamp;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
        this.imgURL = imgURL;
        this.glideCode = glideCode;
        this.localImgUrl = localImgUrl;
        this.localArticleUrl = localArticleUrl;
        this.state = state;
    }

    public long getGdeltmmic_id() {
        return gdeltmmic_id;
    }

    public String getLocalImgUrl() {
        return localImgUrl;
    }

    public void setLocalImgUrl(String localImgUrl) {
        this.localImgUrl = localImgUrl;
    }

    public String getLocalArticleUrl() {
        return localArticleUrl;
    }

    public void setLocalArticleUrl(String localArticleUrl) {
        this.localArticleUrl = localArticleUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getGlideCode() {
        return glideCode;
    }

    public void setGlideCode(String glideCode) {
        this.glideCode = glideCode;
    }
}
