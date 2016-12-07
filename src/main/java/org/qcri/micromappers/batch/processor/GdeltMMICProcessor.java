package org.qcri.micromappers.batch.processor;


import org.qcri.micromappers.entity.GdeltMMIC;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jlucas on 11/29/16.
 */
public class GdeltMMICProcessor implements ItemProcessor<GdeltMMIC, GdeltMMIC> {

    @Override
    public GdeltMMIC process(GdeltMMIC gdeltMMIC) throws Exception {

        GdeltMMIC transformedGdeltMMIC = null;
        //HttpDownloadUtility httpDownloadUtility;
        try{
            System.out.println("******************************************************************");
       //public GdeltMMIC(String languageCode, String articleURL, String timestamp, String location, String lat, String lon, String imgURL, String glidecode)
            transformedGdeltMMIC = new GdeltMMIC(gdeltMMIC.getLanguageCode(), gdeltMMIC.getArticleURL(), gdeltMMIC.getTimestamp(), gdeltMMIC.getLocation(), gdeltMMIC.getLat(), gdeltMMIC.getLon(), gdeltMMIC.getImgURL(), gdeltMMIC.getGlideCode());
           // HttpDownloadUtility.downloadFile(gdeltMMIC.getImgURL(), FilePathSpec.GDELT_IMAGE_PATH, null);
            System.out.println("Converting (" + gdeltMMIC + ") into (" + transformedGdeltMMIC + ")");


        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return transformedGdeltMMIC;

    }
}
