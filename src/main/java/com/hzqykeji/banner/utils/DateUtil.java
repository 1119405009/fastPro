package com.hzqykeji.banner.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


  public static final String DATE_FORMAT_START = "yyyyMMddHHmm";
  public static final long FIVE_MINUTE = 5 * 60 * 1000;
  public static final long EIGHT_MINUTE = 8 * 60 * 1000;
  public static final long TEN_MINUTE = 10 * 60 * 1000;
  public static final long ONE_DAY = 24 * 60 * 60 * 1000;
  static final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_START);


  public static Date fomatDateStart(String date) {
    try {
      return format.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }


  public static boolean compareDate(String s, String e) {
    if (fomatDateStart(s) == null || fomatDateStart(e) == null) {
      return false;
    }
    return fomatDateStart(s).getTime() >= fomatDateStart(e).getTime();
  }


  /**
   * yyyyMMddHHmm
   *
   * @return
   */
  public static String getFormatDate(String formatDate) {
    DateFormat fmt = new SimpleDateFormat(formatDate);
    return fmt.format(new Date());
  }

  /**
   * 获取过期时间
   *
   * @param date
   * @return
   */
  public static long getPrefixTime(String date, long raiseDate) {
    if ((fomatDateStart(date).getTime() - new Date().getTime() - raiseDate) > 0) {
      return fomatDateStart(date).getTime() - new Date().getTime() - raiseDate;
    }
    return 0;
  }

  /**
   * 当前时间距离明天凌晨的秒数
   *
   * @return
   */
  public static Long getSecondsNextEarlyMorning() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_YEAR, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
  }


  public static String getDateStr(Date date) {
    return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
  }

  public static Date getDateByStr(String date) {
    try {
      return format.parse(date);
    } catch (ParseException e) {
      return new Date();
    }
  }
}
