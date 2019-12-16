package com.chujian.wapp.navigator.utils;

import com.chujian.wapp.navigator.common.Constants;
import com.chujian.wapp.navigator.sso.model.AccessToken;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionUtils {

  public static void noCache(HttpServletResponse response) {
    response.setDateHeader("Expires", 0);
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.addHeader("Cache-Control", "post-check=0, pre-check=0");
    response.setHeader("Pragma", "no-cache");
  }

  public static void removeAccessToken(HttpServletRequest request) {
    HttpSession session = request.getSession();
    session.removeAttribute(Constants.SESSION_KEY_ACCESS_TOKEN);
    session.removeAttribute(Constants.SESSION_KEY_ACCESS_TOKEN_STR);
  }

  public static void saveAccessToken(HttpServletRequest request, AccessToken accessToken,
      String accessTokenStr)
      throws Exception {
    HttpSession session = request.getSession();
    session.setAttribute(Constants.SESSION_KEY_ACCESS_TOKEN, accessToken);
    session.setAttribute(Constants.SESSION_KEY_ACCESS_TOKEN_STR, accessTokenStr);
  }

  public static AccessToken getAccessToken(HttpServletRequest request) {
    HttpSession session = request.getSession();
    return (AccessToken) session.getAttribute(Constants.SESSION_KEY_ACCESS_TOKEN);
  }

  public static String getAccessTokenStr(HttpServletRequest request) {
    HttpSession session = request.getSession();
    return (String) session.getAttribute(Constants.SESSION_KEY_ACCESS_TOKEN_STR);
  }
}
