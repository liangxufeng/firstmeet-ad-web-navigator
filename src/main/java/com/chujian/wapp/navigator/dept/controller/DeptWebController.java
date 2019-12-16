package com.chujian.wapp.navigator.dept.controller;

import com.chujian.wapp.navigator.sso.model.ACL;
import com.chujian.wapp.navigator.common.acl.AclResource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class DeptWebController {

  @AclResource(resourceId = ACL.MENU_DEPT)
  @RequestMapping(value = "/dept_manage")
  public String systeManage(HttpServletRequest request) {
    return "dept/dept_query";
  }
}
