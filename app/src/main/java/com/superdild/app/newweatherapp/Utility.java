package com.superdild.app.newweatherapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gino on 23/03/18.
 */

public class Utility {



  public static String DateFormatHHmm(long data){
     SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
      String dataNew = formatter.format(new Date(data*1000L));

  return dataNew;
  }

  public static String DateFormat(long data){
      SimpleDateFormat formatter = new SimpleDateFormat("EEE dd, HH:mm", Locale.getDefault());
      String dataformat = formatter.format(new Date(data * 1000L));

      return dataformat;
  }

  public static String DateFormatExtended(long data){
      SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMMM", Locale.getDefault());
      String dataformat = formatter.format(new Date(data * 1000L));

      return dataformat;
  }
}
