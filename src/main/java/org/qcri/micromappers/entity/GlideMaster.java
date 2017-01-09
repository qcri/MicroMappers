package org.qcri.micromappers.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jlucas on 1/8/17.
 */
@Entity
@Table(name = "glide_master")
public class GlideMaster extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;

    @Column(name = "glide_code", nullable = false, length = 500)
    private String glideCode;

    @Column(nullable=false, name="updated")
    private Timestamp updated;

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
}
