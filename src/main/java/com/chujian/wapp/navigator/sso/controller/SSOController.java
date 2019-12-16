package com.chujian.wapp.navigator.sso.controller;

import com.chujian.wapp.navigator.common.Constants;
import com.chujian.wapp.navigator.resource.model.ResourceTypeConfig;
import com.chujian.wapp.navigator.resource.respository.ResourceRepository;
import com.chujian.wapp.navigator.sso.service.AccessControlService;
import com.chujian.wapp.navigator.sso.service.AccessTokenService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RefreshScope
@RequestMapping("sso")
public class SSOController {

  @Resource
  private AccessTokenService accessTokenService;

  @Resource
  private AccessControlService accessControlService;

  @Autowired
  private ResourceRepository resourceRepository;

  @RequestMapping(value = "auth", method = {RequestMethod.GET, RequestMethod.POST})
  public String ssoAuth(HttpServletRequest request,
      @RequestParam(name = "sys_id", required = false) String systemId,
      @RequestParam(name = "url", required = false) String url) throws Exception {

    request.setAttribute("sys_id", "");

    if (StringUtils.isNotBlank(systemId)) {
      com.chujian.wapp.navigator.resource.entity.Resource resource = resourceRepository
          .findByResourceId(systemId);
      if (resource != null && ResourceTypeConfig.RS_TYPE_SYSTEM
          .equals(resource.getResourceType())) {
        request.setAttribute("sys_id", String.valueOf(resource.getId()));
      }
    }
    if (StringUtils.isNotBlank(url)) {
      return "redirect:/system_index?url=" + url;
    }
    return "login";
  }

  @RequestMapping(value = "sys", method = {RequestMethod.GET, RequestMethod.POST})
  public String accessSystem(HttpServletRequest request,
      @RequestParam("sys_id") Long systemId) {

    com.chujian.wapp.navigator.resource.entity.Resource resource = resourceRepository
        .findByIdKey(systemId);
    if (resource == null || !ResourceTypeConfig.RS_TYPE_SYSTEM.equals(resource.getResourceType())) {
      return "redirect:/";
    }
/*
    com.chujian.wapp.navigator.resource.entity.Resource parentResource = resourceRepository
        .findByIdKey(resource.getParentId());
    if (parentResource == null) {
      if (!accessControlService
          .access(request, resource.getResourceId(), resource.getResourceId())) {
        return "redirect:/";
      }
    } else {
      if (!accessControlService
          .access(request, parentResource.getResourceId(), resource.getResourceId())) {
        return "redirect:/";
      }
    }*/

    String resourceUrl = resource.getResourceUrl();
    if (StringUtils.isBlank(resourceUrl)) {
      return "redirect:/";
    }

    String authCode = accessTokenService.generateAuthCode(request);
    return "redirect:" + resourceUrl + "?" + Constants.REQUEST_PARAM_CODE + "=" + authCode;
  }
}
