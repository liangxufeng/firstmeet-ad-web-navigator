package com.chujian.wapp.navigator.sso.service;

import com.chujian.wapp.navigator.common.Constants;
import com.chujian.wapp.navigator.sso.model.ACL;
import com.chujian.wapp.navigator.resource.model.ResourceTypeConfig;
import com.chujian.wapp.navigator.role.entity.RoleResource;
import com.chujian.wapp.navigator.role.respository.RoleResourceRepository;
import com.chujian.wapp.navigator.sso.model.AccessResource;
import com.chujian.wapp.navigator.sso.model.AccessRole;
import com.chujian.wapp.navigator.sso.model.AccessToken;
import com.chujian.wapp.navigator.utils.SessionUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("acl")
public class AccessControlService {

  @Resource
  private AccessTokenService accessTokenService;

  @Autowired
  private RoleResourceRepository roleResourceRepository;


  public String resourceName(HttpServletRequest request, Long resourceId) {
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    if (!accessTokenService.isValid(accessToken)) {
      return "";
    }
    List<AccessResource> resourceList = accessToken.getResourceList();
    if (resourceList == null || resourceList.isEmpty()) {
      return "";
    }

    List<AccessRole> roleList = accessToken.getRoleList();
    if (roleList == null || roleList.isEmpty()) {
      return "";
    }
    List<Long> resourceIdList = new ArrayList<>();
    for (AccessRole accessRole : roleList) {
      List<RoleResource> roleResourceList = roleResourceRepository
          .findByRoleId(accessRole.getId());
      if (roleResourceList == null || roleResourceList.isEmpty()) {
        return "";
      }
      for (RoleResource roleResource : roleResourceList) {
        resourceIdList.add(roleResource.getResourceId());
      }
    }
    for (Long oldResourceId : resourceIdList) {
      if (oldResourceId.equals(resourceId)) {
        return oldResourceId.toString();
      }
    }
    return "";
  }


  public boolean access(HttpServletRequest request, String systemId, String resourceId) {
    //入参: systemId  resourceId
    //查询出systemId所包含的所有的资源resourceId的集合
    //判断集合是否有传入的resourceId  有:返回true  没有:返回false
    AccessToken accessToken = SessionUtils.getAccessToken(request);
    if (!accessTokenService.isValid(accessToken)) {
      return false;
    }

    List<AccessResource> resourceList = accessToken.getResourceList();
    if (resourceList == null || resourceList.isEmpty()) {
      return false;
    }

    List<AccessResource> sysSecondList = new ArrayList<>();//二级系统集合
    for (AccessResource accessResource : resourceList) {
      if (accessResource.getResourceId().equals(resourceId)) {
        return true;
      }
      if (accessResource.getResourceId().equals(systemId)) {
        sysSecondList.add(accessResource);
      }
    }

    if (sysSecondList.isEmpty()) {
      return false;
    }

    for (AccessResource accessResource : sysSecondList) {
      if (accessResource.getResourceId().equals(resourceId)) {
        return true;
      }
      //查询出accessResource包含的所有的资源
      //遍历查询包含的所有的资源集合中是否包含传入的resourceId
      List<AccessResource> allResourceList = new ArrayList<>();
      List<AccessResource> accessResourceList = getAllResourceList(resourceList, allResourceList);
      if (accessResourceList == null) {
        return false;
      }
      for (AccessResource resource : accessResourceList) {
        if (resource.getResourceId().equals(resourceId)) {
          return true;
        }
      }
    }
    return false;
  }

  private List<AccessResource> getAllResourceList(List<AccessResource> resourceList,
      List<AccessResource> allResourceList) {
    if (resourceList != null) {
      for (AccessResource accessResource : resourceList) {
        allResourceList.add(accessResource);
        List<AccessResource> childList = accessResource.getChildList();
        getAllResourceList(childList, allResourceList);
      }
    }
    return allResourceList;
  }

  public boolean accessMenu(HttpServletRequest request , String resourceId){

    if (resourceId==null){
      return false;
    }
    if (accessSys(request,resourceId)){
      return true;
    }

    AccessResource resource = systemResource(request);
    if (resource == null) {
      return false;
    }
    List<AccessResource> childList = resource.getChildList();
    if (childList == null) {
      return false;
    }
    List<AccessResource> allResourceList = new ArrayList<>();
    List<AccessResource> list = allResource(childList, allResourceList);
    for (AccessResource accessResource : list) {
      if (resourceId.equals(accessResource.getResourceId())) {
        return true;
      }
    }
    return false;
  }

  private Boolean accessSys(HttpServletRequest request,String resourceId) {
    AccessToken accessToken = (AccessToken) request.getSession()
        .getAttribute(Constants.SESSION_KEY_ACCESS_TOKEN);
    if (accessToken == null) {
      return false;
    }
    List<AccessResource> resourceList = accessToken.getResourceList();
    if (resourceList == null) {
      return false;
    }
    for (AccessResource accessResource : resourceList) {
      if (accessResource.getResourceId().equals(resourceId)){
        return true;
      }
    }
    return false;
  }

  private List<AccessResource> allResource(List<AccessResource> childList,
      List<AccessResource> allResourceList) {
    for (AccessResource resource : childList) {
      allResourceList.add(resource);
      if (resource.getChildList() != null) {
        allResource(resource.getChildList(), allResourceList);
      }
    }
    return allResourceList;
  }

  private AccessResource systemResource(HttpServletRequest request) {
    AccessToken accessToken = (AccessToken) request.getSession()
        .getAttribute(Constants.SESSION_KEY_ACCESS_TOKEN);
    if (accessToken == null) {
      return null;
    }
    List<AccessResource> sysResourceList = new ArrayList<>();
    List<AccessResource> resourceList = accessToken.getResourceList();
    if (resourceList == null) {
      return null;
    }
    for (AccessResource accessResource : resourceList) {
      if (ResourceTypeConfig.RS_TYPE_SYSTEM.equals(accessResource.getType())) {
        sysResourceList.add(accessResource);
        List<AccessResource> childList = accessResource.getChildList();
        if (childList != null) {
          for (AccessResource resource : childList) {
            if (ResourceTypeConfig.RS_TYPE_SYSTEM.equals(resource.getType())) {
              sysResourceList.add(resource);
              List<AccessResource> child2List = resource.getChildList();
              if (child2List != null) {
                for (AccessResource resource2 : child2List) {
                  if (ResourceTypeConfig.RS_TYPE_SYSTEM.equals(resource2.getType())) {
                    sysResourceList.add(resource2);
                  }
                }
              }
            }
          }
        }
      }
    }

    if (sysResourceList.isEmpty()) {
      return null;
    }
    for (AccessResource resource : sysResourceList) {
      if (ACL.SYS_SYS.equals(resource.getResourceId())) {
        return resource;
      }
    }
    return null;
  }
}
