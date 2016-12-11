package org.qcri.micromappers.entity;

import javax.persistence.*;

/**
 * Created by jlucas on 12/4/16.
 */
@Entity
@Table(name = "gdelt3w")
public class Gdelt3W {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long gdelt3w_id;

    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "article_url", nullable = false)
    private String articleURL;

    @Column(name = "img_url")
    private String imgURL;

    @Column(name = "glide_code", nullable = false)
    private String glideCode;

    @Column(name = "local_img_url")
    private String localImgUrl;

    @Column(name = "local_article_url")
    private String localArticleUrl;

    @Column(name = "state")
    private String state;

    @Column(name = "wheres")
    private String wheres;

    @Column(name = "who")
    private String who;

    public Gdelt3W() {
    }

    public Gdelt3W(String languageCode, String articleURL, String glideCode, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.glideCode = glideCode;
        this.state = state;
    }


    public Gdelt3W(String languageCode, String articleURL, String imgURL, String glideCode, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.glideCode = glideCode;
        this.state = state;
    }

    public Gdelt3W(String languageCode, String articleURL, String imgURL, String glideCode, String localImgUrl, String localArticleUrl, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.glideCode = glideCode;
        this.localImgUrl = localImgUrl;
        this.localArticleUrl = localArticleUrl;
        this.state = state;
    }


    public String getWheres() {
        return wheres;
    }

    public void setWheres(String wheres) {
        this.wheres = wheres;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public long getGdelt3w_id() {
        return gdelt3w_id;
    }

    public void setGdelt3w_id(long gdelt3w_id) {
        this.gdelt3w_id = gdelt3w_id;
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

}
