package org.qcri.micromappers.utility;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util
{
 // final private static long MAX_CHECK_TIME_MILLIS = 10800000;  // 3 hours
  final private static long MAX_CHECK_TIME_MILLIS = 3600000; // 1hr
  private static long timeOfLastTranslationProcessingMillis = System.currentTimeMillis(); //initialize at startup

  public static long getDurationInMinutes(Date currentTime, Date oldTime)
  {
    long diff = currentTime.getTime() - oldTime.getTime();
    long diffMinutes = diff / 60000L;
    return diffMinutes;
  }

  public static boolean isTimeToSnopesFetchRun(){

    long currentTimeMillis = System.currentTimeMillis();
    // every 3hours
    if ((currentTimeMillis - timeOfLastTranslationProcessingMillis) >= MAX_CHECK_TIME_MILLIS) {
      return true;
    }
    return false;
  }
}
