package com.chujian.wapp.navigator.resource.controller;

import com.chujian.wapp.navigator.sso.model.ACL;
import com.chujian.wapp.navigator.common.acl.AclResource;
import com.chujian.wapp.navigator.resource.service.ResourceRequestService;
import com.chujian.wapp.navigator.sso.model.AccessResource;
import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import com.chujian.wapp.navigator.utils.SessionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class ResourceWebController {

  @Autowired
  private ResourceRequestService resourceRequestService;

  @Autowired
  private AccessTokenService accessTokenService;

  @RequestMapping(value = "/system_index")
  public String systemIndex(HttpServletRequest request,
      @RequestParam(value = "url", required = false) String url) {
    if (url != null && accessTokenService.isValid(SessionUtils.getAccessToken(request))) {
      request.setAttribute("url", url);
    }else {
      request.setAttribute("url", 0);
    }
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    List<AccessResource> resourceList = accessToken.getResourceList();
    if (resourceList == null || resourceList.isEmpty()) {
      return "system";
    }
    List<AccessResource> resourceMenuList = new ArrayList<>();
    for (AccessResource accessResource : resourceList) {
      if ("sys".equals(accessResource.getResourceId())) {
        resourceMenuList.add(accessResource);
      }
    }
    request.setAttribute("resourceMenuList", resourceMenuList);
    return "system";
  }

  @AclResource(resourceId = ACL.MENU_RESOURCE)
  @RequestMapping(value = "/resource_manage")
  public String resourceManage(HttpServletRequest request) {
    //跳转页面时将一些数据存到request中
    resourceRequestService.putResourceTypeToRequest(request);
    return "resource/resource_query";
  }
}
