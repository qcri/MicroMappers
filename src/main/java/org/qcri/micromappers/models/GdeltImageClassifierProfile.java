package org.qcri.micromappers.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.qcri.micromappers.entity.GdeltImageClassifier;
import org.qcri.micromappers.utility.GdeltGeoApiParameter;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by jlucas on 5/1/17.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class GdeltImageClassifierProfile implements Serializable {

    private static final long serialVersionUID = 9061444052309716511L;

    private String theme;
    private String location;
    private String locationCC;
    private String imageWebTag;
    private String imageTag;

    private String geoJsonQuery;
    private String imageGeoJsonQuery;
    private String htmlQuery;
    private String imageHtmlQuery;


    public GdeltImageClassifierProfile(String theme, String location, String locationCC, String imageWebTag, String imageTag) {
        this.theme = theme;
        this.location = location;
        this.locationCC = locationCC;
        this.imageTag = imageTag;
        this.imageWebTag = imageWebTag;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationCC() {
        return locationCC;
    }

    public void setLocationCC(String locationCC) {
        this.locationCC = locationCC;
    }

    public String getImageWebTag() {
        return imageWebTag;
    }

    public void setImageWebTag(String imageWebTag) {
        this.imageWebTag = imageWebTag;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getGeoJsonQuery() {
        return geoJsonQuery;
    }

    public void setGeoJsonQuery() {
        this.geoJsonQuery = this.assembleQuery() + GdeltGeoApiParameter.DISPLAY_FORMAT + GdeltGeoApiParameter.FORMAT_GEO_JSON;
    }

    public String getImageGeoJsonQuery() {
        return imageGeoJsonQuery;
    }

    public void setImageGeoJsonQuery() {
        this.imageGeoJsonQuery = this.assembleQuery() + GdeltGeoApiParameter.DISPLAY_FORMAT + GdeltGeoApiParameter.FORMAT_IMAGE_GEO_JSON;
    }

    public String getHtmlQuery() {
        return htmlQuery;
    }

    public void setHtmlQuery() {
        this.htmlQuery = this.assembleQuery() + GdeltGeoApiParameter.DISPLAY_FORMAT + GdeltGeoApiParameter.FORMAT_HTML;
    }

    public String getImageHtmlQuery() {
        return imageHtmlQuery;
    }

    public void setImageHtmlQuery() {
        this.imageHtmlQuery = this.assembleQuery() + GdeltGeoApiParameter.DISPLAY_FORMAT + GdeltGeoApiParameter.FORMAT_IMAGE_HTML;
    }

    private String assembleQuery() {
            //theme,  location,  locationCC,  imageWebTag,  imageTag
        boolean addOR = false;
        StringBuffer sb = new StringBuffer();

        sb.append("(");
    /**
        if(this.theme!= null && !this.theme.isEmpty()){
            sb.append(this.reformatQuery(GdeltGeoApiParameter.QUERY_THEME, this.theme));
            addOR = true;
        }

        if(this.locationCC!= null && !this.locationCC.isEmpty()){
            if(addOR){
                sb.append(" OR ");
                addOR = false;
            }

            sb.append(this.reformatQuery(GdeltGeoApiParameter.QUERY_LOCATION_CC, this.locationCC));

            addOR = true;
        }
**/

        if(this.imageWebTag!= null && !this.imageWebTag.isEmpty()){
            if(addOR){
                sb.append(" OR ");
                addOR = false;
            }

            sb.append(this.reformatQuery(GdeltGeoApiParameter.QUERY_IMAGE_WEB_TAG, this.imageWebTag));

            addOR = true;
        }

        if(this.imageTag!= null && !this.imageTag.isEmpty()){
            if(addOR){
                sb.append(" OR ");
                addOR = false;
            }
            sb.append(this.reformatQuery(GdeltGeoApiParameter.QUERY_IMAGE_TAG, this.imageTag));

            addOR = true;
        }

        if(this.location!= null && !this.location.isEmpty()){
            if(addOR){
                sb.append(" OR ");
                addOR = false;
            }
            sb.append(this.reformatQuery(GdeltGeoApiParameter.QUERY_LOCATION, this.location));

        }
        sb.append(")");
        String query = "";
        try {
            query = URLEncoder.encode(sb.toString(), "UTF-8");
        }
        catch (Exception e){

        }

        //sb.append(GdeltGeoApiParameter.GDELT_GEO_API_URL);
       // sb.append("&mode=ImagePointData&");

        return GdeltGeoApiParameter.GDELT_GEO_API_URL + query + "&mode=ImagePointData&";
    }

    private String reformatQuery(String requestParameterTopic, String requestString){
        if(requestString.contains(",")){
            String[] tokens = requestString.split(",");
            StringBuffer reqStr = new StringBuffer();
            for(int i=0; i < tokens.length; i++){
                reqStr.append(requestParameterTopic);
                reqStr.append("\"");
                reqStr.append(tokens[i]);
                reqStr.append("\"");
                reqStr.append(" OR ");

            }

            return  reqStr.toString();
        }
        return requestParameterTopic + "\"" +requestString + "\"";

    }

}
