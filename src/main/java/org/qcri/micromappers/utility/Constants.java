package org.qcri.micromappers.utility;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Constants
{
  private static Logger logger = Logger.getLogger(Constants.class);

  public static long ONE_HOUR_IN_MILLISECS = 3600000L;
  
  public static final String GDELT_PROCESSED_EXTENTION = ".processed";
  public static final String GDELT_3W_SIGNATURE = "3W";
  public static final String GDELT_MMIC_SIGNATURE = "MMIC";
  public static final String SNOPES_STATE_ACTIVE = "active";
  public static final String GDELT_3W_MMIC_PROCESSED = "processed";
  
  public static final String SYSTEM_USER_NAME = "System";
  
  public static final Integer DEFAULT_PAGE_SIZE = 10;
  public static final Integer DEFAULT_NAVIGATION_PAGE_SIZE = 10;

  public static List<String> STOP_WORDS;

  public static void populateStopWords() throws Exception {
    InputStream is = new ClassPathResource("stopwords_minimal.txt").getInputStream();
    List<String> stopWords= new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is)))
    {
      stopWords = Arrays.asList(br.lines().collect(Collectors.toList()).get(0).split(","));

    } catch (IOException e) {
      //e.printStackTrace();
      logger.error("populateStopWords : " + e);
    }

    STOP_WORDS = stopWords;

  }
}
