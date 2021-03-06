package org.qcri.micromappers.entity;

import javax.persistence.*;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;

/**
 * Created by jlucas on 12/4/16.
 */
@Entity
@Table(name = "gdelt3w")
public class Gdelt3W  extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name = "language_code", nullable = false, length = 50)
    private String languageCode;

    @Column(name = "article_url", nullable = false, length = 1000)
    private String articleURL;

    @Column(name = "img_url", length = 1000)
    private String imgURL;

    @Column(name = "glide_code", nullable = false, length = 500)
    private String glideCode;

    @Column(name = "local_img_url", length = 1000)
    private String localImgUrl;

    @Column(name = "local_article_url", length = 1000)
    private String localArticleUrl;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "wheres", columnDefinition="TEXT")
    private String wheres;

    @Column(name = "who", length = 1000)
    private String who;

    @Transient
    private JSONArray jsWheres;

    @Column(name="computer_vision_enabled", columnDefinition = "boolean default false", nullable = false)
    private Boolean computerVisionEnabled;

    @OneToOne(mappedBy = "gdelt3W")
    ImageAnalyserTask imageAnalyserTask;


    public Gdelt3W() {
    }

    public Gdelt3W(String languageCode, String articleURL, String glideCode, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.glideCode = glideCode;
        this.state = state;
        this.computerVisionEnabled = false;
    }


    public Gdelt3W(String languageCode, String articleURL, String imgURL, String glideCode, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.glideCode = glideCode;
        this.state = state;
        this.computerVisionEnabled = false;
    }

    public Gdelt3W(String languageCode, String articleURL, String imgURL, String glideCode, String localImgUrl, String localArticleUrl, String state) {
        this.languageCode = languageCode;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.glideCode = glideCode;
        this.localImgUrl = localImgUrl;
        this.localArticleUrl = localArticleUrl;
        this.state = state;
        this.computerVisionEnabled = false;
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


    public JSONArray getJsWheres() {
        return jsWheres;
    }

    public void setJsWheres(JSONArray jsWheres) {
        this.jsWheres = jsWheres;
    }

    public Boolean getComputerVisionEnabled() {
        return computerVisionEnabled;
    }

    public void setComputerVisionEnabled(Boolean computerVisionEnabled) {
        this.computerVisionEnabled = computerVisionEnabled;
    }

    public ImageAnalyserTask getImageAnalyserTask() {
        return imageAnalyserTask;
    }

    public void setImageAnalyserTask(ImageAnalyserTask imageAnalyserTask) {
        this.imageAnalyserTask = imageAnalyserTask;
    }

    @Override
    public String toString(){
        String seperator = ",";
        String end_line="\r\n";

        StringBuffer sb = new StringBuffer();

        sb.append(StringEscapeUtils.escapeCsv(this.glideCode));

        sb.append(seperator);

        sb.append(StringEscapeUtils.escapeCsv(this.languageCode));

        sb.append(seperator);

        sb.append(StringEscapeUtils.escapeCsv(this.articleURL));

        sb.append(seperator);


        sb.append(StringEscapeUtils.escapeCsv(this.imgURL));

        sb.append(seperator);


        sb.append(StringEscapeUtils.escapeCsv(this.who));
        sb.append(seperator);


        if(this.getWheres() !=null){
            sb.append(StringEscapeUtils.escapeCsv(StringEscapeUtils.escapeJson(this.getWheres())));
        }
        else{
            sb.append("");
        }

        sb.append(end_line);

        return sb.toString();
    }
}
