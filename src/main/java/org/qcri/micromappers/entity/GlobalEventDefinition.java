package org.qcri.micromappers.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by jlucas on 12/9/16.
 */
@Entity
@Table(name="global_event_definition")
public class GlobalEventDefinition extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name="title", length = 500)
    private String title;

    @Column(name="description", length = 2000)
    private String description;

    @Column(name="claim_reviewed", length = 1000)
    private String claimReviewed;

    @Column(name="clain_review_rating", length = 50)
    private String clainReviewRating;

    @Column(name="article_tag", length = 1000)
    private String articleTag;

    @Column(name="search_keyword", length = 2000)
    private String searchKeyword;

    @Column(name="event_url", length = 1000)
    private String eventUrl;

    @Column(name="author", length = 250)
    private String author;

    @Column(name="date_published", length = 100)
    private String datePublished;

    @Column(name="state", length = 50)
    private String state;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "global_event_definition_id")
    private List<Collection> collection;


    public GlobalEventDefinition(String title, String description, String claimReviewed, String clainReviewRating, String articleTag, String searchKeyword, String eventUrl, String author, String datePublished) {
        this.title = title;
        this.description = description;
        this.claimReviewed = claimReviewed;
        this.clainReviewRating = clainReviewRating;
        this.articleTag = articleTag;
        this.searchKeyword = searchKeyword;
        this.eventUrl = eventUrl;
        this.author = author;
        this.datePublished = datePublished;
    }

    public GlobalEventDefinition() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClaimReviewed() {
        return claimReviewed;
    }

    public void setClaimReviewed(String claimReviewed) {
        this.claimReviewed = claimReviewed;
    }

    public String getClainReviewRating() {
        return clainReviewRating;
    }

    public void setClainReviewRating(String clainReviewRating) {
        this.clainReviewRating = clainReviewRating;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }
}
