package org.qcri.micromappers.classifier.image;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;

/**
 * Created by jlucas on 1/23/17.
 */
public class MSCognitiveService {
    protected static Logger logger = Logger.getLogger(MSCognitiveService.class);
    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();

    public void analyzeImage(String imageURL){
        if(!this.isQualifiedImage(imageURL)){
            return;
        }

        HttpClient httpclient = HttpClients.createDefault();

        try
        {

            URIBuilder builder = new URIBuilder(configProperties.getProperty(MicromappersConfigurationProperty.MICROSOFT_CONGNITIVE_VISION_API_ENDPOINT));

            builder.setParameter("visualFeatures", "Description,Categories,Tags,Adult");
            builder.setParameter("language", "en");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", configProperties.getProperty(MicromappersConfigurationProperty.MICROSOFT_CONGNITIVE_VISION_API_SUBSCRIPTION_KEY));

            JSONObject o = new JSONObject();
            o.put("url",imageURL );

            StringEntity reqEntity = new StringEntity(o.toJSONString(),"application/json", "UTF-8");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // need to save to DB
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }

    private boolean isQualifiedImage(String imageURL){
        if(!imageURL.toLowerCase().contains("jpeg") && !imageURL.toLowerCase().contains("jpg")
                && !imageURL.toLowerCase().contains("png")
                && !imageURL.toLowerCase().contains("gif") && !imageURL.toLowerCase().contains("bmp")){
            return false;
        }

        try {
            HttpURLConnection.setFollowRedirects(false);

            HttpURLConnection con =
                    (HttpURLConnection) new URL(imageURL).openConnection();
            con.setRequestMethod("HEAD");
            if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                return false;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
