package org.qcri.micromappers.utility;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util
{
  public static long getDurationInMinutes(Date currentTime, Date oldTime)
  {
    long diff = currentTime.getTime() - oldTime.getTime();
    long diffMinutes = diff / 60000L;
    return diffMinutes;
  }

  public static boolean isTimeToSnopesFetchRun(){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    String currentTime = LocalTime.now().format(formatter);

    if(currentTime.equalsIgnoreCase(LocalTime.of(0, 0).format(formatter))){
      return true;
    }
    if(currentTime.equalsIgnoreCase(LocalTime.of(6, 0).format(formatter))){
      return true;

    }
    if(currentTime.equalsIgnoreCase(LocalTime.of(12, 0).format(formatter))){
      return true;
    }
    if(currentTime.equalsIgnoreCase(LocalTime.of(18,0).format(formatter))){
      return true;
    }

    return false;
  }
}
