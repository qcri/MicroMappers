package org.qcri.micromappers.batch.processor;

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
	private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();
	
    @Override
    public Gdelt3W process(Gdelt3W gdelt3W) throws Exception {
        String imgFileURL = gdelt3W.getImgURL();
        if(imgFileURL != null){
            if(imgFileURL.contains("http//cdn.")){
                System.out.println("cdn found : " + imgFileURL);
                System.out.println("cdn indexx : " + imgFileURL.indexOf("cdn."));
                System.out.println("cdn imgFileURL.length() : " + imgFileURL.length());
                System.out.println("cdn substring : " + imgFileURL.substring(imgFileURL.indexOf("cdn."), imgFileURL.length()));

                imgFileURL = imgFileURL.substring(imgFileURL.indexOf("cdn."), imgFileURL.length());

            }
            String imgFileName = imgFileURL.substring(imgFileURL.lastIndexOf("/") + 1, imgFileURL.length());

            System.out.println("imgFileURL : " + imgFileURL);


            imgFileName = Constants.GDELT_3W_SIGNATURE + "_" + gdelt3W.getGdelt3w_id()+ "_" + imgFileName;

            System.out.println("imgFileName : " + imgFileName);
            HttpDownloadUtility.UserAgentBasedDownloadFile(gdelt3W.getImgURL(), configProperties.getProperty(MicromappersConfigurationProperty.GDELT_IMAGE_PATH), imgFileName);

            gdelt3W.setLocalImgUrl(imgFileName);
        }

        String articleFileURL = gdelt3W.getArticleURL();

        if(articleFileURL != null){

            if(articleFileURL.lastIndexOf("/") == articleFileURL.length()){
                articleFileURL = articleFileURL.substring(0, articleFileURL.length() - 1) ;
            }

            String articleFileName = articleFileURL.substring(articleFileURL.lastIndexOf("/") + 1, articleFileURL.length());

            articleFileName = Constants.GDELT_3W_SIGNATURE + "_" + gdelt3W.getGdelt3w_id()+ "_" + articleFileName;

            System.out.println("articleFileURL : " + articleFileURL);
            System.out.println("articleFileName : " + articleFileName);

            HttpDownloadUtility.UserAgentBasedDownloadFile(gdelt3W.getArticleURL() , configProperties.getProperty(MicromappersConfigurationProperty.GDELT_ARTICLE_PATH), articleFileName);

            gdelt3W.setLocalArticleUrl(articleFileName);
        }

        gdelt3W.setState("processed");

        return gdelt3W;
    }
}

