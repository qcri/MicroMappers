package org.qcri.micromappers.batch.mapper;


import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * Created by jlucas on 11/28/16.
 */
public class GdeltMMICFieldSetMapper implements FieldSetMapper<GdeltMMIC> {

    private static Logger logger = Logger.getLogger(GdeltMMICFieldSetMapper.class);

    @Override
    public GdeltMMIC mapFieldSet(FieldSet fieldSet) throws BindException {
       //languageCode,articleURL,timestamp,location,lat,lon,imgURL,glidecode
        //System.out.println("fieldSet : " + fieldSet.getFieldCount());
        GdeltMMIC gdeltMaster = new GdeltMMIC();
        if(fieldSet.getFieldCount() > 8){
            logger.info("GdeltMMIC Field size is greater 8 : actual size - " + fieldSet.getFieldCount());
            return null;
        }
        else {
            if(!this.validateGlideCode(fieldSet.readRawString(7))){
                return null;
            }

            gdeltMaster.setLanguageCode(fieldSet.readRawString(0));
            gdeltMaster.setArticleURL(fieldSet.readRawString(1));
            gdeltMaster.setTimestamp(fieldSet.readRawString(2));
            gdeltMaster.setLocation(fieldSet.readRawString(3));
            gdeltMaster.setLat(fieldSet.readRawString(4));
            gdeltMaster.setLon(fieldSet.readRawString(5));
            //System.out.println(fieldSet.readRawString(6));
            //System.out.println(fieldSet.readRawString(6).lastIndexOf("http://"));
            int httpIndex = fieldSet.readRawString(6).lastIndexOf("http://");
            int httpsIndex = fieldSet.readRawString(6).lastIndexOf("https://");

            if(httpIndex > 0 || httpsIndex>0){
                if(httpIndex > 0) {
                    gdeltMaster.setImgURL(fieldSet.readRawString(6).substring(httpIndex));
                }
                else{
                    gdeltMaster.setImgURL(fieldSet.readRawString(6).substring(httpsIndex));
                }
            }
            else{
                gdeltMaster.setImgURL(fieldSet.readRawString(6));
            }

            //gdeltMaster.setImgURL(fieldSet.readRawString(6).substring(fieldSet.readRawString(6).lastIndexOf("http:")));
            gdeltMaster.setGlideCode(fieldSet.readRawString(7));
        }
        return gdeltMaster;
    }

    private boolean validateGlideCode(String glideCode){

        if(glideCode.length() < 18 || glideCode.length() > 18){
            return false;
        }

        if(glideCode.split("-").length < 2){
            return false;
        }

        return true;

    }

}
