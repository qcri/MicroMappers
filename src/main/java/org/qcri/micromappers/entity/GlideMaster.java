package org.qcri.micromappers.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by jlucas on 1/8/17.
 */
@Entity
@Table(name = "glide_master")
public class GlideMaster extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name = "glide_code", nullable = false)
    private String glideCode;

    @Column(nullable=false, name="updated")
    private Timestamp updated;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "glide_master_id")
    private List<Collection> collection;

    @OneToMany
    @JoinColumn(name = "glide_code", referencedColumnName = "glide_code")
    private List<Gdelt3W> Gdelt3WList;

    @OneToMany
    @JoinColumn(name = "glide_code", referencedColumnName = "glide_code")
    private List<GdeltMMIC> GdeltMMICList;

    public GlideMaster() {
    }

    public String getGlideCode() {
        return glideCode;
    }

    public void setGlideCode(String glideCode) {
        this.glideCode = glideCode;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    public List<Gdelt3W> getGdelt3WList() {
        return Gdelt3WList;
    }

    public void setGdelt3WList(List<Gdelt3W> gdelt3WList) {
        Gdelt3WList = gdelt3WList;
    }

    public List<GdeltMMIC> getGdeltMMICList() {
        return GdeltMMICList;
    }

    public void setGdeltMMICList(List<GdeltMMIC> gdeltMMICList) {
        GdeltMMICList = gdeltMMICList;
    }

    @JsonIgnore
    public long getTotalCount3WImage() {
        long totalCount3WImage = 0;

        if(Gdelt3WList.size() > 0){
            totalCount3WImage =  Gdelt3WList.stream().filter(line -> line.getImgURL() != null)
                    .collect(Collectors.toList()).size();
        }

        return totalCount3WImage;
    }

    @JsonIgnore
    public long getTotalCountMMICImage() {
        long totalCountMMICImage = 0;

        if(GdeltMMICList.size() > 0) {
            totalCountMMICImage = GdeltMMICList.stream().filter(line -> line.getImgURL() != null)
                    .collect(Collectors.toList()).size();
        }

        return totalCountMMICImage;
    }

    @JsonIgnore
    public long getTotalCount3WArticle() {
        long totalCount3WArticle = 0;
        if(Gdelt3WList.size() > 0) {
            totalCount3WArticle = Gdelt3WList.stream().filter(line -> line.getArticleURL() != null)
                    .collect(Collectors.toList()).size();
        }
        return totalCount3WArticle;
    }

    @JsonIgnore
    public long getTotalCountMMICArticle() {
        long totalCountMMICArticle = 0;

        if(GdeltMMICList.size() > 0) {
            totalCountMMICArticle = GdeltMMICList.stream().filter(line -> line.getArticleURL() != null)
                    .collect(Collectors.toList()).size();
        }

        return totalCountMMICArticle;
    }

}
