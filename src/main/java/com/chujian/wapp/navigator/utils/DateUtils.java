package com.chujian.wapp.navigator.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DateUtils {

  public static final String TIMEFORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

  public static String convertDate(String dateStr, String originalDateTimeFormat,
      String convertedDateTimeFormat) {
    if (StringUtils.isBlank(dateStr)) {
      return "";
    }
    try {
      ZonedDateTime dateTime = ZonedDateTime
          .from(DateTimeFormatter.ofPattern(originalDateTimeFormat)
              .parse(dateStr));

      return dateTime.format(DateTimeFormatter.ofPattern(convertedDateTimeFormat));

    } catch (Exception ex) {
      log.error("Convert time {} to format {} failed!", dateStr, convertedDateTimeFormat);
      int pos = dateStr.lastIndexOf(".");
      if (pos > 0) {
        return dateStr.replace("T", " ").substring(0, pos);
      }
    }
    return "";
  }

  public static String getCurrentDateTimeWithZone() {
    return ZonedDateTime.now().format(DateTimeFormatter
        .ofPattern
            (TIMEFORMAT_WITH_TIMEZONE));
  }


  public static String getCurrentDateTime() {
    return ZonedDateTime.now().format(DateTimeFormatter
        .ofPattern
            (TIMEFORMAT));
  }

  public static String formatDateTime(Date date, String dateTimeFormat) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter
            .ofPattern
                (dateTimeFormat));
  }

  public static Date parseZonedDateTime(String dataTimeStr, String dateTimeFormat) {
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dataTimeStr, DateTimeFormatter
        .ofPattern
            (dateTimeFormat));
    return Date.from(zonedDateTime.toInstant());
  }

  public static Date parseDateTime(String dataTimeStr, String dateTimeFormat) {
    LocalDateTime localDateTime = LocalDateTime.parse(dataTimeStr, DateTimeFormatter
        .ofPattern
            (dateTimeFormat));
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date minusMinutes(Date dateTime, int value) {
    return Date.from(LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault())
        .minusMinutes
            (value).atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date plusMinutes(Date dateTime, int value) {
    return Date.from(LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault())
        .plusMinutes
            (value).atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date minusSceonds(Date dateTime, int value) {
    return Date.from(LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault())
        .minusSeconds(value).atZone(ZoneId.systemDefault()).toInstant());
  }

  public static Date plusSceonds(Date dateTime, int value) {
    return Date.from(LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault())
        .plusSeconds(value).atZone(ZoneId.systemDefault()).toInstant());
  }

}
