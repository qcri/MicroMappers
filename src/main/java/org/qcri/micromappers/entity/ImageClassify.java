package org.qcri.micromappers.entity;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.math.BigDecimal;
/**
 * Created by jlucas on 1/25/17.
 */
@Entity
@Table(name="image_classify")
public class ImageClassify extends ExtendedBaseEntity {
    private static final long serialVersionUID = -881973526366597368L;
    private static Logger logger = Logger.getLogger(ImageClassify.class);

    @Column(name = "name", length = 500)
    String name;

    @Column(name = "score", precision = 20, scale =20)
    BigDecimal score;

    @ManyToOne
    @JoinColumn(name="image_analysis_id", nullable = false)
    private ImageAnalysis imageAnalysis;


    public ImageClassify() {
    }

    public ImageClassify(String name, BigDecimal score) {
        this.name = name;
        this.score = score;
    }

    public ImageClassify(String name, BigDecimal score, ImageAnalysis imageAnalysis) {
        this.name = name;
        this.score = score;
        this.imageAnalysis = imageAnalysis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public ImageAnalysis getImageAnalysis() {
        return imageAnalysis;
    }

    public void setImageAnalysis(ImageAnalysis imageAnalysis) {
        this.imageAnalysis = imageAnalysis;
    }

}
