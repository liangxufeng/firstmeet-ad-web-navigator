package com.chujian.wapp.navigator.sso.controller;

import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import com.chujian.wapp.navigator.syslog.entity.SysOpLog;
import com.chujian.wapp.navigator.syslog.service.SysOpLogService;
import com.chujian.wapp.navigator.utils.SessionUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class CommonWebController {

  @Resource
  private AccessTokenService accessTokenService;

  @Autowired
  private SysOpLogService opLogService;

  @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
  public String login(HttpServletRequest request) {
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    if (accessTokenService.isValid(accessToken)) {
      return "redirect:/index";
    } else {
      return "redirect:/sso/auth";
    }
  }

  @RequestMapping(value = "/index")
  public String index(HttpServletRequest request,
      @RequestParam(value = "url", required = false) String url) {
    if (url != null && !"index".equals(url)) {
      return "redirect:"+url;
    }
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    request.setAttribute("token", accessToken);
    return "index";
  }

  @RequestMapping(value = "/welcome")
  public String welcome(HttpServletRequest request) {
    return "welcome";
  }

  @RequestMapping(value = "logout", method = {RequestMethod.GET, RequestMethod.POST})
  public String logout(HttpServletRequest request) {
    opLogService.saveOpLog(request, "user", "logout", "user logout", SysOpLog.OP_RESULT_OK);

    SessionUtils.removeAccessToken(request);
    request.getSession().invalidate();
    return "redirect:/sso/auth";
  }

  @RequestMapping(value = "/password_change")
  public String changePassword(HttpServletRequest request) {
    return "sso/password_change";
  }
}
