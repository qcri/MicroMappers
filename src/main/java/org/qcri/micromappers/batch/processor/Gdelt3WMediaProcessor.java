package org.qcri.micromappers.batch.processor;

import org.apache.log4j.Logger;
import org.qcri.micromappers.entity.Gdelt3W;
import org.qcri.micromappers.utility.Constants;
import org.qcri.micromappers.utility.HttpDownloadUtility;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created by jlucas on 12/5/16.
 */
public class Gdelt3WMediaProcessor implements ItemProcessor<Gdelt3W, Gdelt3W> {
    private static Logger logger = Logger.getLogger(Gdelt3WMediaProcessor.class);
    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
	
    @Override
    public Gdelt3W process(Gdelt3W gdelt3W) throws Exception {

        gdelt3W = this.processImageFile(gdelt3W);

        gdelt3W = this.processArticleFile(gdelt3W);

        gdelt3W.setState("processed");

        return gdelt3W;
    }

    private Gdelt3W processImageFile(Gdelt3W gdelt3W){
        String imgFileURL = gdelt3W.getImgURL();
        if(imgFileURL != null){
            if(imgFileURL.contains("//cdn.")){
                imgFileURL = imgFileURL.substring(imgFileURL.indexOf("cdn."));

            }
            String imgFileName = imgFileURL.substring(imgFileURL.lastIndexOf("/") + 1, imgFileURL.length());

            imgFileName = Constants.GDELT_3W_SIGNATURE + "_" + gdelt3W.getId()+ "_" + imgFileName;

            HttpDownloadUtility.UserAgentBasedDownloadFile(gdelt3W.getImgURL(), configProperties.getProperty(MicromappersConfigurationProperty.GDELT_IMAGE_PATH), imgFileName);

            gdelt3W.setLocalImgUrl(imgFileName);
        }
        return gdelt3W;

    }

    private Gdelt3W processArticleFile(Gdelt3W gdelt3W){
        String articleFileURL = gdelt3W.getArticleURL();

        if(articleFileURL != null){
            String articleFileName = Constants.GDELT_3W_SIGNATURE + "_" + gdelt3W.getId();

            HttpDownloadUtility.UserAgentBasedDownloadFile(gdelt3W.getArticleURL() , configProperties.getProperty(MicromappersConfigurationProperty.GDELT_ARTICLE_PATH), articleFileName);

            gdelt3W.setLocalArticleUrl(articleFileName);
        }
        return gdelt3W;
    }
}

