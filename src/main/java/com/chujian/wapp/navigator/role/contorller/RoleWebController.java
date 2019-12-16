package com.chujian.wapp.navigator.role.contorller;

import com.chujian.wapp.navigator.sso.model.ACL;
import com.chujian.wapp.navigator.common.acl.AclResource;
import com.chujian.wapp.navigator.role.service.RoleRequestService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class RoleWebController {

  @Autowired
  private RoleRequestService roleRequestService;

  @AclResource(resourceId = ACL.MENU_ROLE)
  @RequestMapping(value = "/role_manage")
  public String systeManage(HttpServletRequest request) {
    return "role/role_query";
  }

  @RequestMapping(value = "/role_permission")
  public String resourceRoleManage(HttpServletRequest request, String roleId) {
    roleRequestService.putRoleToRequest(request, roleId);
    return "role/role_resource";
  }

  @RequestMapping(value = "/role_game")
  public String gameRoleManage(HttpServletRequest request, String roleId) {
    roleRequestService.putRoleToRequest(request, roleId);
    return "role/role_game";
  }

  @RequestMapping(value = "/role_data")
  public String dataRoleManage(HttpServletRequest request, String roleId) {
    roleRequestService.putRoleDataToRequest(request, roleId);
    return "role/role_data";
  }

}
