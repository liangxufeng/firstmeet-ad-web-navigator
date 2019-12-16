package com.chujian.wapp.navigator.common.mvc;

import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import com.chujian.wapp.navigator.utils.SessionUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
public class AccessControlInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    AccessTokenService accessTokenService = (AccessTokenService) request.getServletContext()
        .getAttribute("accessTokenService");
    AccessToken accessToken = SessionUtils.getAccessToken(request);

    if (!accessTokenService.isValid(accessToken) && !isAllowAccessPage(request)) {
      response.sendRedirect("/");
      return false;
    }
    return true;
  }

  private boolean isAllowAccessPage(HttpServletRequest request) throws IOException {
    String requestUri = request.getRequestURI();
    if (StringUtils.isBlank(requestUri)) {
      return true;
    }
    String contextPath = request.getContextPath();

    int startPos = 0;
    if (StringUtils.isNotBlank(contextPath) && !contextPath.equals("/") && requestUri
        .startsWith(contextPath)) {
      startPos = contextPath.length();
    }

    int position = requestUri.indexOf("/", startPos);
    if (position < 0) {
      return true;
    }
    int paraPosition = requestUri.indexOf("?");
    if (paraPosition < 0) {
      paraPosition = requestUri.length();
    }
    if (position >= paraPosition) {
      return true;
    }
    String url = requestUri.substring(position, paraPosition);
    if (url.length() == 0) {
      return true;
    }
    if (url.equals("/")) {
      return true;
    }
    String[] allowedUrls = {"/sso/auth", "/sso/token", "/logout", "/sso/login", "/sso/depts",
        "/sso/menus", "/sso/check_token", "/sso/logout"};
    for (String allowedUrl : allowedUrls) {
      if (url.startsWith(allowedUrl)) {
        return true;
      }
    }
    return false;
  }

}
