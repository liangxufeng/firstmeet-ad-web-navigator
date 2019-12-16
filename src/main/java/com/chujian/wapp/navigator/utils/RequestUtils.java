package com.chujian.wapp.navigator.utils;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {

  public static String getRequestIp(HttpServletRequest request) {
    String ip = null;
    String[] ipHeaders = {"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR", "X-Real-IP"};
    for (String ipHeader : ipHeaders) {
      ip = request.getHeader(ipHeader);
      if (isValid(ip)) {
        // 多次反向代理后会有多个ip值，第一个ip才是真实ip
        if (ip.contains(",")) {
          ip = ip.split(",")[0];
          if (isValid(ip)) {
            break;
          }
        } else {
          break;
        }
      }
    }

    if (!isValid(ip)) {
      ip = request.getRemoteAddr();
    }

    return ip;
  }

  private static boolean isValid(String ip) {
    return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip.trim());
  }
}
