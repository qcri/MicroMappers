package org.qcri.micromappers.utility;

import java.util.Date;

public class Util
{
  public static long getDurationInMinutes(Date currentTime, Date oldTime)
  {
    long diff = currentTime.getTime() - oldTime.getTime();
    long diffMinutes = diff / 60000L;
    return diffMinutes;
  }
}
