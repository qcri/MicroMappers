package org.qcri.micromappers.utility;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Constants
{
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
    String filePath = new ClassPathResource("stopwords_minimal.txt").getFile().getAbsolutePath();
    List<String> stopWords= new ArrayList<>();
    try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath), Charset.forName("UTF-8")))
    {
      stopWords = Arrays.asList(br.lines().collect(Collectors.toList()).get(0).split(","));

    } catch (IOException e) {
      e.printStackTrace();
    }

    STOP_WORDS = stopWords;

  }
}
