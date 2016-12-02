package org.qcri.micromappers.batch.processor;

import org.qcri.micromappers.model.GdeltMMIC;
import org.qcri.micromappers.utility.FilePathSpec;
import org.qcri.micromappers.utility.HttpDownloadUtility;
import org.springframework.batch.item.ItemProcessor;


/**
 * Created by jlucas on 12/2/16.
 */
public class GdeltMMICMediaProcessor implements ItemProcessor<GdeltMMIC, GdeltMMIC> {

    @Override
    public GdeltMMIC process(GdeltMMIC gdeltMMIC) throws Exception {
        String imgFileURL = gdeltMMIC.getImgURL();
        if(imgFileURL != null){
            if(imgFileURL.indexOf("http//cdn.") > -1){
                System.out.println("cdn found : " + imgFileURL);
                System.out.println("cdn indexx : " + imgFileURL.indexOf("cdn."));
                System.out.println("cdn imgFileURL.length() : " + imgFileURL.length());
                System.out.println("cdn substring : " + imgFileURL.substring(imgFileURL.indexOf("cdn."), imgFileURL.length()));

                imgFileURL = imgFileURL.substring(imgFileURL.indexOf("cdn."), imgFileURL.length());

            }
            String imgFileName = imgFileURL.substring(imgFileURL.lastIndexOf("/") + 1, imgFileURL.length());

            System.out.println("imgFileURL : " + imgFileURL);


            imgFileName = gdeltMMIC.getGdeltmmic_id()+ "_" + imgFileName;

            System.out.println("imgFileName : " + imgFileName);
            HttpDownloadUtility.UserAgentBasedDownloadFile(gdeltMMIC.getImgURL(), FilePathSpec.GDELT_IMAGE_PATH, imgFileName);

            gdeltMMIC.setLocalImgUrl(imgFileName);
        }

        String articleFileURL = gdeltMMIC.getArticleURL();

        if(articleFileURL != null){

            if(articleFileURL.lastIndexOf("/") == (articleFileURL.length() -1)){
                articleFileURL = articleFileURL.substring(0, articleFileURL.length() - 1) ;
            }

            String articleFileName = articleFileURL.substring(articleFileURL.lastIndexOf("/") + 1, articleFileURL.length());

            articleFileName = gdeltMMIC.getGdeltmmic_id()+ "_" + articleFileName;

            System.out.println("articleFileURL : " + articleFileURL);
            System.out.println("articleFileName : " + articleFileName);

            HttpDownloadUtility.UserAgentBasedDownloadFile(gdeltMMIC.getArticleURL() , FilePathSpec.GDELT_ARTICLE_PATH, articleFileName);

            gdeltMMIC.setLocalArticleUrl(articleFileName);
        }

        gdeltMMIC.setState("processed");

        return gdeltMMIC;
    }
}
