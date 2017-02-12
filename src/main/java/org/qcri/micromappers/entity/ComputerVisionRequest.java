package org.qcri.micromappers.entity;

import javax.persistence.*;

/**
 * Created by jlucas on 2/10/17.
 */
@Entity
@Table(name = "computer_vision_request")
public class ComputerVisionRequest extends ExtendedBaseEntity {

    private static final long serialVersionUID = 1705153546973813717L;

    @Column(name="state", nullable=false)
    String state;

    @Column(name="collection_id", nullable=true)
    long collectionId;

    @Column(name="data_feed_id", nullable=true)
    long dataFeedId;

    @Column(name="glide_master_id", nullable=true)
    long glideMasterId;

    @Column(name = "account_id", nullable=false)
    long accountId;

    public ComputerVisionRequest(String state, long collectionId, long dataFeedId, long glideMasterId, long accountId) {
        this.state = state;
        this.collectionId = collectionId;
        this.dataFeedId = dataFeedId;
        this.glideMasterId = glideMasterId;
        this.accountId = accountId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }

    public long getDataFeedId() {
        return dataFeedId;
    }

    public void setDataFeedId(long dataFeedId) {
        this.dataFeedId = dataFeedId;
    }

    public long getGlideMasterId() {
        return glideMasterId;
    }

    public void setGlideMasterId(long glideMasterId) {
        this.glideMasterId = glideMasterId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
