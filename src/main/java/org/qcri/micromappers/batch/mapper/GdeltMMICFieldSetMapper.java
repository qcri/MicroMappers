package org.qcri.micromappers.batch.mapper;


import org.qcri.micromappers.model.GdeltMMIC;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Created by jlucas on 11/28/16.
 */
public class GdeltMMICFieldSetMapper implements FieldSetMapper<GdeltMMIC> {

    @Override
    public GdeltMMIC mapFieldSet(FieldSet fieldSet) throws BindException {
       //languageCode,articleURL,timestamp,location,lat,lon,imgURL,glidecode
        System.out.println("fieldSet : " + fieldSet.getFieldCount());
        GdeltMMIC gdeltMaster = new GdeltMMIC();
        if(fieldSet.getFieldCount() > 8){
            gdeltMaster.setLanguageCode(fieldSet.readRawString(0));
            gdeltMaster.setArticleURL(fieldSet.readRawString(1) + fieldSet.readRawString(2));
            gdeltMaster.setTimestamp(fieldSet.readRawString(3));
            gdeltMaster.setLocation(fieldSet.readRawString(4));
            gdeltMaster.setLat(fieldSet.readRawString(5));
            gdeltMaster.setLon(fieldSet.readRawString(6));
            gdeltMaster.setImgURL(fieldSet.readRawString(7)+fieldSet.readRawString(8));
            gdeltMaster.setGlideCode(fieldSet.readRawString(9));
        }
        else {
            gdeltMaster.setLanguageCode(fieldSet.readRawString(0));
            gdeltMaster.setArticleURL(fieldSet.readRawString(1));
            gdeltMaster.setTimestamp(fieldSet.readRawString(2));
            gdeltMaster.setLocation(fieldSet.readRawString(3));
            gdeltMaster.setLat(fieldSet.readRawString(4));
            gdeltMaster.setLon(fieldSet.readRawString(5));
            gdeltMaster.setImgURL(fieldSet.readRawString(6));
            gdeltMaster.setGlideCode(fieldSet.readRawString(7));
        }
        return gdeltMaster;
    }

}
