package org.qcri.micromappers.utility;

import org.apache.log4j.Logger;

/**
 * Created by jlucas on 5/1/17.
 */
public class GdeltGeoApiParameter {
    private static Logger logger = Logger.getLogger(Constants.class);

    public static final String DISPLAY_FORMAT = "format=";
    public static final String FORMAT_HTML = "html";
    public static final String FORMAT_IMAGE_HTML = "imagehtml";
    public static final String FORMAT_GEO_JSON = "geojson";
    public static final String FORMAT_IMAGE_GEO_JSON = "imagegeojson";

    public static final String QUERY_REQUEST = "query=";
    public static final String QUERY_THEME = "theme:";
    public static final String QUERY_LOCATION="location:";
    public static final String QUERY_LOCATION_CC = "locationcc:";

    public static final String QUERY_IMAGE_TAG = "imagetag:";
    public static final String QUERY_IMAGE_WEB_TAG = "imagewebtag:";

    public static final Integer QUERY_TONE = 5;
    public static final Integer QUERY_MIN_TIME_SPAN = 15;
    public static final Integer QUERY_MAX_TIME_SPAN = 1440;

    public static final String GDELT_GEO_API_URL = "http://api.gdeltproject.org/api/v2/geo/geo?query=";

}
