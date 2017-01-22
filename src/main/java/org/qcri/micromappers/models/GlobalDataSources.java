package org.qcri.micromappers.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.qcri.micromappers.entity.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jlucas on 1/16/17.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GlobalDataSources implements Serializable {

    private static final long serialVersionUID = 6329919374565981089L;

    private String source;
    private GlobalEventDefinition globalEventDefinition;
    private GlideMaster glideMaster;
    private long socialCollectionTotal;
    private long twitterCollectionTotal;
    private long facebookCollectionTotal;
    private long gdeltCollectionTotal;
    private long gdelt3WImageTotal;
    private long gdeltMMICImageTotal;
    private long gdelt3WArticleTotal;
    private long gdeltMMICArticleTotal;
    private List<CollectionDetailsInfo> collectionDetailsInfoList;
    private List<Gdelt3W> gdelt3WList;
    private List<GdeltMMIC> gdeltMMICList;
    private List<WordCloud> keywords;
    private Timestamp createdAt;

    public GlobalDataSources() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public GlobalEventDefinition getGlobalEventDefinition() {
        return globalEventDefinition;
    }

    public void setGlobalEventDefinition(GlobalEventDefinition globalEventDefinition) {
        this.globalEventDefinition = globalEventDefinition;
    }

    public GlideMaster getGlideMaster() {
        return glideMaster;
    }

    public void setGlideMaster(GlideMaster glideMaster) {
        this.glideMaster = glideMaster;
    }

    public long getSocialCollectionTotal() {
        return socialCollectionTotal;
    }

    public void setSocialCollectionTotal(long socialCollectionTotal) {
        this.socialCollectionTotal = socialCollectionTotal;
    }

    public long getTwitterCollectionTotal() {
        return twitterCollectionTotal;
    }

    public void setTwitterCollectionTotal(long twitterCollectionTotal) {
        this.twitterCollectionTotal = twitterCollectionTotal;
    }

    public long getFacebookCollectionTotal() {
        return facebookCollectionTotal;
    }

    public void setFacebookCollectionTotal(long facebookCollectionTotal) {
        this.facebookCollectionTotal = facebookCollectionTotal;
    }

    public long getGdeltCollectionTotal() {
        return gdeltCollectionTotal;
    }

    public long getGdelt3WImageTotal() {
        return gdelt3WImageTotal;
    }

    public void setGdelt3WImageTotal(long gdelt3WImageTotal) {
        this.gdelt3WImageTotal = gdelt3WImageTotal;
    }

    public long getGdeltMMICImageTotal() {
        return gdeltMMICImageTotal;
    }

    public void setGdeltMMICImageTotal(long gdeltMMICImageTotal) {
        this.gdeltMMICImageTotal = gdeltMMICImageTotal;
    }

    public long getGdelt3WArticleTotal() {
        return gdelt3WArticleTotal;
    }

    public void setGdelt3WArticleTotal(long gdelt3WArticleTotal) {
        this.gdelt3WArticleTotal = gdelt3WArticleTotal;
    }

    public long getGdeltMMICArticleTotal() {
        return gdeltMMICArticleTotal;
    }

    public void setGdeltMMICArticleTotal(long gdeltMMICArticleTotal) {
        this.gdeltMMICArticleTotal = gdeltMMICArticleTotal;
    }

    public void setGdeltCollectionTotal(long gdeltCollectionTotal) {
        this.gdeltCollectionTotal = gdeltCollectionTotal;
    }

    public List<CollectionDetailsInfo> getCollectionDetailsInfoList() {
        return collectionDetailsInfoList;
    }

    public void setCollectionDetailsInfoList(List<CollectionDetailsInfo> collectionDetailsInfoList) {
        this.collectionDetailsInfoList = collectionDetailsInfoList;
    }

    public List<Gdelt3W> getGdelt3WList() {
        return gdelt3WList;
    }

    public void setGdelt3WList(List<Gdelt3W> gdelt3WList) {
        this.gdelt3WList = gdelt3WList;
    }

    public List<GdeltMMIC> getGdeltMMICList() {
        return gdeltMMICList;
    }

    public void setGdeltMMICList(List<GdeltMMIC> gdeltMMICList) {
        this.gdeltMMICList = gdeltMMICList;
    }

    public List<WordCloud> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<WordCloud> keywords) {
        this.keywords = keywords;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
