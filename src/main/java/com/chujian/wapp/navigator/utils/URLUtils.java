package com.chujian.wapp.navigator.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class URLUtils {

  public static String urlEncoded(String str) {
    try {
      return URLEncoder.encode(str, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      log.error("Url encoded failed:", ex);
    }
    return str;
  }
}
