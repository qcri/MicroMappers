package org.qcri.micromappers.batch.processor;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.GdeltMMIC;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.HttpDownloadUtility;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.batch.item.ItemProcessor;


/**
 * Created by jlucas on 12/2/16.
 */
public class GdeltMMICMediaProcessor implements ItemProcessor<GdeltMMIC, GdeltMMIC> {
    private static Logger logger = Logger.getLogger(GdeltMMICMediaProcessor.class);
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

    @Override
    public GdeltMMIC process(GdeltMMIC gdeltMMIC) throws Exception {
        String imgFileURL = gdeltMMIC.getImgURL();
        if(imgFileURL != null){
            if(imgFileURL.contains("//cdn.")){
                imgFileURL = imgFileURL.substring(imgFileURL.indexOf("cdn."));

            }
            String imgFileName = imgFileURL.substring(imgFileURL.lastIndexOf("/") + 1, imgFileURL.length());

            imgFileName = Constants.GDELT_MMIC_SIGNATURE + "_"+ gdeltMMIC.getId()+ "_" + imgFileName;

            HttpDownloadUtility.UserAgentBasedDownloadFile(gdeltMMIC.getImgURL(), configProperties.getProperty(MicromappersConfigurationProperty.GDELT_IMAGE_PATH), imgFileName);

            gdeltMMIC.setLocalImgUrl(imgFileName);
        }

        String articleFileURL = gdeltMMIC.getArticleURL();

        if(articleFileURL != null){

            if(articleFileURL.endsWith("/")){
                articleFileURL = articleFileURL.replaceAll("/+$","") ;
            }

            String articleFileName = articleFileURL.substring(articleFileURL.lastIndexOf("/") + 1, articleFileURL.length());

            articleFileName = Constants.GDELT_MMIC_SIGNATURE + "_"+ gdeltMMIC.getId()+ "_" + articleFileName;


            HttpDownloadUtility.UserAgentBasedDownloadFile(gdeltMMIC.getArticleURL() , configProperties.getProperty(MicromappersConfigurationProperty.GDELT_ARTICLE_PATH), articleFileName);

            gdeltMMIC.setLocalArticleUrl(articleFileName);
        }

        gdeltMMIC.setState("processed");

        return gdeltMMIC;
    }
}
