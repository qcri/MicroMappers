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
import org.qcri.micromappers.utility.ComputerVisionStatus;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurationProperty;
import org.qcri.micromappers.utility.configurator.MicromappersConfigurator;

/**
 * Created by jlucas on 1/23/17.
 */
public class MSCognitiveClassifier {
    protected static Logger logger = Logger.getLogger(MSCognitiveClassifier.class);
    private static MicromappersConfigurator configProperties = MicromappersConfigurator.getInstance();


    public static String analyzeImage(String imageURL){
        if(!isFormatQualified(imageURL)){
            return ComputerVisionStatus.EXCEPTION_NOT_CORRECT_IMAGE_FORMAT.getStatus();
        }

        if(!isUrlValid(imageURL)){
            return ComputerVisionStatus.EXCEPTION_NOT_VALID_IMAGE_URL.getStatus();
        }

        HttpClient httpclient = HttpClients.createDefault();
        String classifiedResult = null;
        try
        {

            URIBuilder builder = new URIBuilder(configProperties.getProperty(MicromappersConfigurationProperty.MICROSOFT_CONGNITIVE_VISION_API_ENDPOINT));

            builder.setParameter("visualFeatures", "Description,Categories,Tags,Adult");
            builder.setParameter("language", "en");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key",
                    configProperties.getProperty
                            (
                                    MicromappersConfigurationProperty.MICROSOFT_CONGNITIVE_VISION_API_SUBSCRIPTION_KEY
                            ));

            JSONObject o = new JSONObject();
            o.put("url",imageURL );

            StringEntity reqEntity = new StringEntity(o.toJSONString(),"application/json", "UTF-8");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                classifiedResult = EntityUtils.toString(entity);
            }
        }
        catch (Exception e)
        {
            logger.error(e);
            return ComputerVisionStatus.EXCEPTION_UNKNOWN.getStatus();
        }

        return classifiedResult;
    }

    private static boolean isFormatQualified(String imageURL){
        if(!imageURL.toLowerCase().contains("jpeg") && !imageURL.toLowerCase().contains("jpg")
                && !imageURL.toLowerCase().contains("png")
                && !imageURL.toLowerCase().contains("gif") && !imageURL.toLowerCase().contains("bmp")){
            return false;
        }

        return true;
    }

    private static boolean isUrlValid(String imageURL){
        try {
            HttpURLConnection.setFollowRedirects(false);

            HttpURLConnection con =
                    (HttpURLConnection) new URL(imageURL).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
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
